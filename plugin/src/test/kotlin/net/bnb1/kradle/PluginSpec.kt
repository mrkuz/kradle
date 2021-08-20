package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory

abstract class PluginSpec(body: PluginSpec.() -> Unit) : FunSpec({}) {

    @OptIn(ExperimentalPathApi::class)
    val projectDir = createTempDirectory("kradle-test-").toFile()
    val settingsFile
        get() = projectDir.resolve("settings.gradle.kts")
    val buildFile
        get() = projectDir.resolve("build.gradle.kts")
    val buildDir
        get() = projectDir.resolve("build")

    init {
        beforeEach {
            projectDir.deleteRecursively()
            projectDir.mkdirs()
            println("Project dir: ${projectDir.absolutePath}")
        }

        afterSpec { projectDir.deleteRecursively() }

        body()
    }

    fun runTask(task: String, vararg arguments: String) = GradleRunner.create()
        .withProjectDir(projectDir)
        .withPluginClasspath()
        .withArguments(listOf(task) + arguments)
        .build()

    fun addTask(name: String, doLast: String) {
        buildFile.appendText(
            """
            tasks.register("$name") {
                doLast {
                    $doLast
                }
            }
            
            """.trimIndent()
        )
    }

    fun bootstrapAppProject() {
        writeSettingsGradle("app")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "1.0.0-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"
            
            kradle {
                targetJvm.set("11")
            }
            
            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )
    }

    fun writeAppKt(main: String) {
        val sourceDir = projectDir.resolve("src/main/kotlin/com/example")
        sourceDir.mkdirs()
        sourceDir.resolve("App.kt").writeText(
            """
            package com.example
            
            class App {}
            
            fun main() {
                $main
            }
            
            """.trimIndent()
        )
    }

    fun bootstrapLibProject() {
        writeSettingsGradle("lib")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-lib") version "1.0.0-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"
            
            kradle {
                targetJvm.set("11")
            }
            
            """.trimIndent()
        )
    }

    fun writeSettingsGradle(name: String) {
        settingsFile.writeText(
            """
            rootProject.name = "$name"
            """.trimIndent()
        )
    }

    fun gitInit() {
        val git = Git.init().setDirectory(projectDir).call()
        git.add().addFilepattern(".").call()
        git.commit().setMessage("Initial commit").call()
    }
}
