package net.bnb1.kradle.blueprints

import io.kotest.inspectors.forOne
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ApplicationBlueprintTests : PluginSpec({

    test("Check DEV_MODE environment variable") {
        bootstrapAppProject()
        writeAppKt("println(\"DEV_MODE=\" + System.getenv()[\"DEV_MODE\"])")

        val result = runTask("run")
        result.output shouldContain "DEV_MODE=true"
    }

    test("Check MANIFEST.MF") {
        bootstrapAppProject()

        runTask("jar")

        val output = buildDir.resolve("tmp/jar/MANIFEST.MF")
        output.shouldExist()
        val lines = output.readLines()
        lines.forOne { it shouldBe "Main-Class: com.example.AppKt" }
    }

    test("Warn if version is missing") {
        writeSettingsGradle("app")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"

            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )

        val result = runTask("tasks")

        result.output shouldContain "WARNING: Version is not specified"
    }

    test("Warn if group is missing") {
        writeSettingsGradle("app")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "main-SNAPSHOT"
            }
            
            version = "1.0.0"

            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )

        val result = runTask("tasks")

        result.output shouldContain "WARNING: Group is not specified"
    }

    test("Warn if group is invalid") {
        writeSettingsGradle("app")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "main-SNAPSHOT"
            }
            
            version = "1.0.0"
            group = "not_a_valid_group"

            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )

        val result = runTask("tasks")

        result.output shouldContain "WARNING: Group doesn't comply with Java's package name rules"
    }

    test("Run 'dev'") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")

        val result = runTask("dev")
        result.output shouldContain "DEBUG Project root: ${projectDir.absolutePath}" // Agent output
        result.output shouldContain "Hello World"
    }
})
