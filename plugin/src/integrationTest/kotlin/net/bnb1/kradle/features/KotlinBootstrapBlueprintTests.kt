package net.bnb1.kradle.features

import io.kotest.inspectors.forOne
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.PluginSpec

class KotlinBootstrapBlueprintTests : PluginSpec({

    test("Bootstrap app project") {
        writeSettingsGradle("app")
        buildFile.writeText(
            """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"
            
            kradle {
                targetJvm("11")
                mainClass("com.example.demo.App")
            }
            
            """.trimIndent()
        )

        runTask("bootstrap")

        projectDir.resolve(".git").shouldExist()
        projectDir.resolve(".gitignore").shouldExist()
        projectDir.resolve("gradlew").shouldExist()
        projectDir.resolve("src/main/resources").shouldExist()
        projectDir.resolve("src/main/extra").shouldExist()
        projectDir.resolve("src/test/kotlin/com/example/demo").shouldExist()
        projectDir.resolve("src/test/resources").shouldExist()
        projectDir.resolve("src/benchmark/kotlin").shouldExist()
        projectDir.resolve("detekt-config.yml").shouldExist()
        projectDir.resolve("README.md").shouldExist()
        projectDir.resolve("LICENSE").shouldExist()
        projectDir.resolve("project.properties").shouldExist()

        val appKt = projectDir.resolve("src/main/kotlin/com/example/demo/App.kt")
        appKt.shouldExist()

        val lines = appKt.readLines()
        lines.forOne { it shouldBe "package com.example.demo" }
    }

    test("Bootstrap app project (multi-project)") {
        writeMultiProjectSettingsGradle("demo", setOf("app"))
        val buildFile = projectDir.resolve("app/build.gradle.kts")
        buildFile.parentFile.mkdirs()
        writeCompatAppBuildFile(buildFile)

        runTask("bootstrap")

        projectDir.resolve(".git").shouldExist()
        projectDir.resolve(".gitignore").shouldExist()
        projectDir.resolve("gradlew").shouldExist()
        projectDir.resolve("app/src/main/resources").shouldExist()
        projectDir.resolve("app/src/main/extra").shouldExist()
        projectDir.resolve("app/src/test/kotlin/com/example").shouldExist()
        projectDir.resolve("app/src/test/resources").shouldExist()
        projectDir.resolve("app/src/benchmark/kotlin").shouldExist()
        projectDir.resolve("detekt-config.yml").shouldExist()
        projectDir.resolve("README.md").shouldExist()
        projectDir.resolve("LICENSE").shouldExist()
        projectDir.resolve("project.properties").shouldExist()

        val appKt = projectDir.resolve("app/src/main/kotlin/com/example/demo/App.kt")
        appKt.shouldExist()

        val lines = appKt.readLines()
        lines.forOne { it shouldBe "package com.example.demo" }
    }

    test("Bootstrap lib project") {
        bootstrapCompatLibProject()

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
        writeCompatLibBuildFile(buildFile)

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
