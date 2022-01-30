package net.bnb1.kradle

import io.kotest.core.spec.Spec
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import java.nio.file.Path

private const val KRADLE_RO = "/home/gradle/kradle-ro"
private const val KRADLE_RW = "/home/gradle/kradle-rw"

class TestContainer(spec: Spec) {

    val container = KGenericContainer(DockerImageName.parse("gradle:7-jdk17"))
        .withFileSystemBind(System.getenv("PROJECT_ROOT_DIR"), KRADLE_RO, BindMode.READ_ONLY)
        .withFileSystemBind(
            Path.of(System.getenv("PROJECT_DIR"), "var", "gradle").toString(),
            "/home/gradle/.gradle",
            BindMode.READ_WRITE
        )
        .withFileSystemBind(
            Path.of(System.getenv("PROJECT_DIR"), "var", "m2").toString(),
            "/home/gradle/.m2",
            BindMode.READ_WRITE
        )
        .withCommand("sleep", "1h")
        .withCreateContainerCmdModifier { it.withUser("gradle") }!!

    init {
        spec.beforeSpec {
            container.start()
            container.execInContainer("cp", "-rf", KRADLE_RO, KRADLE_RW)
            container.execInContainer("gradle", "-p", KRADLE_RW, "clean", "publishToMavenLocal")
        }

        spec.afterSpec {
            container.stop()
        }
    }

    class KGenericContainer(dockerImageName: DockerImageName) : GenericContainer<KGenericContainer>(dockerImageName)
}
