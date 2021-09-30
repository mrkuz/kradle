package net.bnb1.kradle.plugins

import io.kotest.matchers.file.shouldExist
import net.bnb1.kradle.PluginSpec

class BootstrapLibPluginTests : PluginSpec({

    test("Bootstrap lib project") {
        bootstrapLibProject()

        runTask("bootstrap")

        projectDir.resolve(".git").shouldExist()
        projectDir.resolve(".gitignore").shouldExist()
        projectDir.resolve("gradlew").shouldExist()
        projectDir.resolve("src/main/kotlin/com/example").shouldExist()
        projectDir.resolve("src/main/resources").shouldExist()
        projectDir.resolve("src/main/extra").shouldExist()
        projectDir.resolve("src/test/kotlin/com/example").shouldExist()
        projectDir.resolve("src/test/resources").shouldExist()
        projectDir.resolve("src/benchmark/kotlin").shouldExist()
        projectDir.resolve("detekt-config.yml").shouldExist()
        projectDir.resolve("README.md").shouldExist()
        projectDir.resolve("LICENSE").shouldExist()
        projectDir.resolve("project.properties").shouldExist()
    }

    test("Bootstrap lib project (multi-project)") {
        writeMultiProjectSettingsGradle("demo", setOf("lib"))
        val buildFile = projectDir.resolve("lib/build.gradle.kts")
        buildFile.parentFile.mkdirs()
        writeLibBuildFile(buildFile)

        runTask("bootstrap")

        projectDir.resolve(".git").shouldExist()
        projectDir.resolve(".gitignore").shouldExist()
        projectDir.resolve("gradlew").shouldExist()
        projectDir.resolve("lib/src/main/kotlin/com/example").shouldExist()
        projectDir.resolve("lib/src/main/resources").shouldExist()
        projectDir.resolve("lib/src/main/extra").shouldExist()
        projectDir.resolve("lib/src/test/kotlin/com/example").shouldExist()
        projectDir.resolve("lib/src/test/resources").shouldExist()
        projectDir.resolve("lib/src/benchmark/kotlin").shouldExist()
        projectDir.resolve("detekt-config.yml").shouldExist()
        projectDir.resolve("README.md").shouldExist()
        projectDir.resolve("LICENSE").shouldExist()
        projectDir.resolve("project.properties").shouldExist()
    }
})