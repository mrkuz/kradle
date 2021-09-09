package net.bnb1.kradle.plugins

import io.kotest.matchers.file.shouldExist
import net.bnb1.kradle.PluginSpec

class BootstrapAppPluginTests : PluginSpec({

    test("Bootstrap app project") {
        bootstrapAppProject()

        runTask("bootstrap")

        projectDir.resolve(".git").shouldExist()
        projectDir.resolve(".gitignore").shouldExist()
        projectDir.resolve("gradlew").shouldExist()
        projectDir.resolve("src/main/kotlin/com/example/App.kt").shouldExist()
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
})