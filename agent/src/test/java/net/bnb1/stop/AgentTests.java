package net.bnb1.stop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentTests {

    @TempDir
    File projectDir;
    Agent agent;

    @BeforeEach
    void setUp() {
        agent = new Agent(projectDir.toString());
        Path.of(projectDir.toString(), "src/main/resources").toFile().mkdirs();
        Path.of(projectDir.toString(), "src/main/kotlin").toFile().mkdirs();
    }

    Path projectPath(String subPath) {
        return Path.of(projectDir.toString(), subPath);
    }

    @Test
    void shouldHandleDir() throws Exception {
        projectPath("src/main/kotlin/net/bnb1/demo").toFile().mkdirs();
        projectPath("build").toFile().mkdirs();
        agent.initializeWatcher();

        assertFalse(agent.shouldHandleDir(Path.of("/")));
        assertFalse(agent.shouldHandleDir(projectPath("build")));
        assertFalse(agent.shouldHandleDir(projectPath("non-existing")));
        assertFalse(agent.shouldHandleDir(projectPath("src/test/resources")));
        assertFalse(agent.shouldHandleDir(projectPath("src/test/kotlin")));
        assertTrue(agent.shouldHandleDir(projectPath("src/main/resources")));
        assertTrue(agent.shouldHandleDir(projectPath("src/main/kotlin")));
        assertTrue(agent.shouldHandleDir(projectPath("src/main/kotlin/net/bnb1/demo")));
    }

    @Test
    void shouldHandleFile() throws Exception {
        projectPath("src/main/kotlin/net/bnb1/demo").toFile().mkdirs();
        agent.initializeWatcher();

        assertFalse(agent.shouldHandleFile(Path.of("App.kt")));
        assertFalse(agent.shouldHandleFile(projectPath("src/main/kotlin/net/bnb1/demo/App.c")));
        assertFalse(agent.shouldHandleFile(projectPath("src/test/kotlin/net/bnb1/demo/AppTests.kt")));
        assertTrue(agent.shouldHandleFile(projectPath("src/main/kotlin/net/bnb1/demo/App.kt")));
        assertTrue(agent.shouldHandleDir(projectPath("src/main/resources/config.yml")));
        assertTrue(agent.shouldHandleDir(projectPath("src/main/resources/config.yaml")));
        assertTrue(agent.shouldHandleDir(projectPath("src/main/resources/config.properties")));
        assertTrue(agent.shouldHandleDir(projectPath("src/main/resources/config.xml")));
    }

    @Test
    void compareAndUpdateHash() throws Exception {
        var file = projectPath("src/main/resources/config.properties");
        Files.write(file, "key = before".getBytes(Charset.forName("UTF-8")));
        agent.initializeWatcher();

        assertFalse(agent.compareAndUpdateHash(file));
        Files.write(file, "key = after".getBytes(Charset.forName("UTF-8")));
        assertTrue(agent.compareAndUpdateHash(file));
    }

    @Test
    void detectFileCreated() throws Exception {
        var watcher = agent.initializeWatcher();

        var file = projectPath("src/main/resources/config.properties");
        Files.write(file, "key = value".getBytes(Charset.forName("UTF-8")));
        assertTrue(agent.detectChange(watcher.poll(1, TimeUnit.SECONDS)));
    }

    @Test
    void detectFileDeleted() throws Exception {
        var file = projectPath("src/main/resources/config.properties");
        Files.write(file, "key = value".getBytes(Charset.forName("UTF-8")));
        var watcher = agent.initializeWatcher();

        file.toFile().delete();
        assertTrue(agent.detectChange(watcher.poll(1, TimeUnit.SECONDS)));
    }

    @Test
    void detectFileModified() throws Exception {
        var file = projectPath("src/main/resources/config.properties");
        Files.write(file, "key = before".getBytes(Charset.forName("UTF-8")));
        var watcher = agent.initializeWatcher();

        Files.write(file, "key = after".getBytes(Charset.forName("UTF-8")));
        assertTrue(agent.detectChange(watcher.poll(1, TimeUnit.SECONDS)));
    }

    @Test
    void ignoreUnrelatedFiles() throws Exception {
        projectPath("src/main/kotlin/net/bnb1/demo").toFile().mkdirs();
        projectPath("src/test/kotlin/net/bnb1/demo").toFile().mkdirs();
        var watcher = agent.initializeWatcher();

        var file = projectPath("src/main/kotlin/net/bnb1/demo/App.c");
        Files.write(file, new byte[]{});
        file = projectPath("src/test/kotlin/net/bnb1/demo/AppTests.kt");
        Files.write(file, new byte[]{});

        assertFalse(agent.detectChange(watcher.poll(1, TimeUnit.SECONDS)));
    }
}