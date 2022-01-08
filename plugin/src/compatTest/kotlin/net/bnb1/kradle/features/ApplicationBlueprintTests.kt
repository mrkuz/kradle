package net.bnb1.kradle.features

import io.kotest.inspectors.forOne
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec

class ApplicationBlueprintTests : CompatSpec({

    test("Check MANIFEST.MF") {
        bootstrapCompatAppProject()

        runTask("jar")

        val output = buildDir.resolve("tmp/jar/MANIFEST.MF")
        output.shouldExist()
        val lines = output.readLines()
        lines.forOne { it shouldBe "Main-Class: com.example.demo.AppKt" }
    }

    test("Warn if version is missing") {
        writeSettingsGradle("app")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"

            kradle {
                mainClass("com.example.demo.App")
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
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
            }
            
            version = "1.0.0"

            kradle {
                mainClass("com.example.demo.App")
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
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
            }
            
            version = "1.0.0"
            group = "not_a_valid_group"

            kradle {
                mainClass("com.example.demo.App")
            }
            
            """.trimIndent()
        )

        val result = runTask("tasks")

        result.output shouldContain "WARNING: Group doesn't comply with Java's package name rules"
    }

    test("Run app with @JvmName") {
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
                mainClass("com.example.demo.CustomApp", jvmName = true)
            }
            
            """.trimIndent()
        )

        val sourceDir = projectDir.resolve("src/main/kotlin/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.kt").writeText(
            """
            @file:JvmName("CustomApp")
            package com.example.demo
            
            class App
            
            fun main() {
                println("Hello World")
            }
            
            """.trimIndent()
        )

        val result = runTask("run")

        result.output shouldContain "Hello World"
    }
})
