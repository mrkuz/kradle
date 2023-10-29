package net.bnb1.kradle;

import net.bnb1.kradle.annotations.VisibleForTesting;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.math.BigInteger;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Watches directories and stops application on change.
 */
public final class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        new Agent(
                System.getenv().get("KRADLE_PROJECT_ROOT_DIR"),
                System.getenv().get("KRADLE_AGENT_MODE")).start();
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        var thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("watchAndStop");
        return thread;
    });

    private final Path projectPath;

    private final List<Path> dirIncludes = new ArrayList<>();
    private final List<Pattern> fileIncludes = new ArrayList<>();
    private final List<WatchKey> keys = new ArrayList<>();
    private final Map<Path, String> hashes = new HashMap<>();

    private final ChangeStrategy changeStrategy;

    public Agent(String projectRoot, String mode) {
        if (projectRoot == null) {
            throw new IllegalStateException("Project root not set");
        }
        projectPath = Path.of(projectRoot);
        if (!projectPath.toFile().isDirectory()) {
            throw new IllegalStateException("Project root is not a directory");
        }

        if ("rebuild".equalsIgnoreCase(mode)) {
            changeStrategy = new RebuildStrategy(projectPath);
            debug("Mode: rebuild");
        } else {
            changeStrategy = new ExitStrategy();
            debug("Mode: default");
        }

        debug("Project root: " + projectPath);

        dirIncludes.add(Path.of("src/main/kotlin"));
        dirIncludes.add(Path.of("src/main/java"));
        dirIncludes.add(Path.of("src/main/resources"));

        fileIncludes.add(Pattern.compile(".*\\.kt", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.java", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.properties", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.xml", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.ya?ml", Pattern.CASE_INSENSITIVE));
    }

    @SuppressWarnings({"CallToPrintStackTrace", "InfiniteLoopStatement"})
    public void start() {
        executor.submit(() -> {
            try {
                var watcher = initializeWatcher();
                while (true) {
                    var key = watcher.take();
                    if (detectChange(watcher, key)) {
                        changeStrategy.onChangeDetected();
                    }
                }
            } catch (Exception ex) {
                error("Watch failed");
                ex.printStackTrace();
            }
        });
    }

    @VisibleForTesting
    WatchService initializeWatcher() throws IOException {
        var watcher = FileSystems.getDefault().newWatchService();
        Files.walkFileTree(projectPath, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (shouldHandleDir(dir)) {
                    registerDir(watcher, dir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (shouldHandleFile(file)) {
                    // debug("File: " + file.toAbsolutePath());
                    hashes.put(file, md5(file));
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return watcher;
    }

    private void registerDir(WatchService watcher, Path dir) throws IOException {
        debug("Watching: " + dir.toAbsolutePath());
        var key = dir.register(watcher,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        keys.add(key);
    }

    @VisibleForTesting
    boolean shouldHandleDir(Path dir) {
        if (dirIncludes.isEmpty()) {
            return true;
        }
        return dirIncludes.stream().anyMatch(dir::endsWith) || isParentWatched(dir);
    }

    @VisibleForTesting
    boolean shouldHandleFile(Path file) {
        if (fileIncludes.isEmpty()) {
            return isParentWatched(file);
        }
        if (fileIncludes.stream().anyMatch(pattern -> pattern.matcher(file.getFileName().toString()).matches())) {
            return isParentWatched(file);
        }
        return false;
    }

    private boolean isParentWatched(Path path) {
        if (path.getParent() == null) {
            return false;
        }
        return keys.stream()
                .filter(WatchKey::isValid)
                .map(key -> (Path) key.watchable())
                .anyMatch(p -> path.getParent().endsWith(p));
    }

    @VisibleForTesting
    boolean detectChange(WatchService watcher, WatchKey key) throws IOException {
        if (key == null) {
            return false;
        }
        for (var event : key.pollEvents()) {
            var kind = event.kind();
            if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }

            // debug("Event: " + event.kind());
            var directory = (Path) key.watchable();
            var fileName = (Path) event.context();
            var absolutePath = Path.of(directory.toString(), fileName.toString());

            if (absolutePath.toFile().isDirectory()) {
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    registerDir(watcher, absolutePath);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    debug("Directory deleted: " + absolutePath);
                } else {
                    continue;
                }
            } else {
                if (!shouldHandleFile(absolutePath)) {
                    continue;
                }
                if (kind == StandardWatchEventKinds.ENTRY_MODIFY && !compareAndUpdateHash(absolutePath)) {
                    continue;
                }

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    debug("File created: " + absolutePath);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    debug("File modified: " + absolutePath);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    debug("File deleted: " + absolutePath);
                }
            }

            key.reset();
            return true;
        }

        key.reset();
        return false;
    }

    @VisibleForTesting
    boolean compareAndUpdateHash(Path file) throws IOException {
        var hash = md5(file);
        if (hash.equals(hashes.get(file))) {
            return false;
        }
        hashes.put(file, hash);
        return true;
    }

    private String md5(Path file) throws IOException {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        try (var input = new DigestInputStream(new FileInputStream(file.toFile()), md5)) {
            input.readAllBytes();
            var digest = new BigInteger(1, md5.digest()).toString(16);
            while (digest.length() < 32) {
                digest = "0" + digest;
            }
            return digest;
        }
    }

    private void debug(String message) {
        System.out.println("DEBUG " + message);
    }

    private void error(String message) {
        System.err.println("ERROR " + message);
    }
}