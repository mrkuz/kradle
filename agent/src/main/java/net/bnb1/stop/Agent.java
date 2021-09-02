package net.bnb1.stop;

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

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) throws IOException {
        new Agent().start();
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        var thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("watchAndStop");
        return thread;
    });

    private final List<Path> dirIncludes = new ArrayList<>();
    private final List<Pattern> fileIncludes = new ArrayList<>();

    private final List<WatchKey> keys = new ArrayList<>();
    private final Map<Path, String> hashes = new HashMap<>();

    public Agent() {
        dirIncludes.add(Path.of("src/main/kotlin"));
        dirIncludes.add(Path.of("src/main/java"));
        dirIncludes.add(Path.of("src/main/resources"));

        fileIncludes.add(Pattern.compile(".*\\.kt", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.java", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.properties", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.xml", Pattern.CASE_INSENSITIVE));
        fileIncludes.add(Pattern.compile(".*\\.ya?ml", Pattern.CASE_INSENSITIVE));
    }

    public void start() throws IOException {
        var path = getProjectRoot();
        var watcher = FileSystems.getDefault().newWatchService();
        debug("Stop-Agent started: " + path);

        registerRecursive(path, watcher);
        executor.submit(() -> {
            try {
                watch(watcher);
            } catch (Exception ex) {
                System.err.println("ERROR Watch failed");
                ex.printStackTrace();
            }
        });
    }

    private Path getProjectRoot() {
        var path = Path.of(System.getenv().get("PROJECT_ROOT"));
        if (path == null) {
            throw new IllegalStateException("PROJECT_ROOT not set");
        }
        if (!path.toFile().isDirectory()) {
            throw new IllegalStateException("PROJECT_ROOT is not a directory");
        }
        return path;
    }

    private void registerRecursive(Path path, WatchService watcher) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (shouldHandleDir(dir) || isParentWatched(dir)) {
                    debug("Watching: " + dir.toAbsolutePath());
                    var key = dir.register(watcher,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY);
                    keys.add(key);
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

    }

    private void watch(WatchService watcher) throws InterruptedException, IOException {
        while (true) {
            var key = watcher.take();
            for (var event : key.pollEvents()) {
                var kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                // debug("Event: " + event.kind());
                var directory = (Path) key.watchable();
                var fileName = (Path) event.context();
                var absolutePath = Path.of(directory.toString(), fileName.toString());

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

                System.exit(0);
            }

            key.reset();
        }
    }

    private boolean shouldHandleDir(Path dir) {
        if (dirIncludes.isEmpty()) {
            return true;
        }
        return dirIncludes.stream().anyMatch(path -> dir.endsWith(path));
    }

    private boolean shouldHandleFile(Path file) {
        if (fileIncludes.isEmpty()) {
            return isParentWatched(file);
        }
        if (fileIncludes.stream().anyMatch(pattern -> pattern.matcher(file.getFileName().toString()).matches())) {
            return isParentWatched(file);
        }
        return false;
    }

    private boolean isParentWatched(Path path) {
        return keys.stream()
                .map(key -> (Path) key.watchable())
                .anyMatch(p -> path.getParent().endsWith(p));
    }

    private boolean compareAndUpdateHash(Path file) throws IOException {
        var hash = md5(file);
        if (hash.equals(hashes.get(file))) {
            return false;
        }
        hashes.put(file, hash);
        return true;
    }

    private String md5(Path file) throws IOException {
        MessageDigest md5 = null;
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
}