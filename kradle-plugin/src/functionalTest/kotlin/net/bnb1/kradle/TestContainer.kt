package net.bnb1.kradle

import io.kotest.core.spec.Spec
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.Container
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import java.nio.file.Path

private const val KRADLE_RO = "/home/gradle/kradle-ro"
private const val KRADLE_RW = "/home/gradle/kradle-rw"

class TestContainer(spec: Spec) {

    private val container = KGenericContainer(DockerImageName.parse("gradle:8-jdk17"))
        .withFileSystemBind(System.getenv("KRADLE_PROJECT_ROOT_DIR"), KRADLE_RO, BindMode.READ_ONLY)
        .withFileSystemBind(
            Path.of(System.getenv("KRADLE_PROJECT_DIR"), "var", "gradle").toString(),
            "/home/gradle/.gradle",
            BindMode.READ_WRITE
        )
        .withFileSystemBind(
            Path.of(System.getenv("KRADLE_PROJECT_DIR"), "var", "m2").toString(),
            "/home/gradle/.m2",
            BindMode.READ_WRITE
        )
        .withCommand("sleep", "1h")
        .withCreateContainerCmdModifier { it.withUser("gradle") }!!

    init {
        spec.afterSpec {
            container.stop()
        }
    }

    fun bindResource(resource: String, to: String = resource) {
        container.withFileSystemBind(
            javaClass.getResource("/$resource").path,
            "/home/gradle/$to",
            BindMode.READ_ONLY
        )
    }

    fun exec(vararg command: String): Container.ExecResult {
        val result = container.execInContainer(*command)
        if (result.exitCode != 0) {
            println("stdout: ${result.stdout}")
            println("stderr: ${result.stderr}")
        }
        return result
    }

    fun start(): TestContainer {
        container.start()
        container.execInContainer("git", "clone", KRADLE_RO, KRADLE_RW)
        container.execInContainer("gradle", "-p", KRADLE_RW, "clean", "publishToMavenLocal")
        return this
    }

    fun get() = container

    class KGenericContainer(dockerImageName: DockerImageName) : GenericContainer<KGenericContainer>(dockerImageName)
}
