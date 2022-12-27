package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory

abstract class CompatSpec(body: CompatSpec.() -> Unit) : FunSpec({}) {

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
        .forwardOutput()
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

    fun bootstrapProject(name: String = "test", kradleConfig: () -> String) {
        writeSettingsGradle(name)
        writeBuildFile(buildFile, kradleConfig)
    }

    fun writeBuildFile(output: File, kradleConfig: () -> String) = output.writeText(
        """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"
            
            kradle {
                jvm.configureOnly {
                    targetJvm("11")
                }
                ${kradleConfig()}
            }
            
        """.trimIndent()
    )

    fun bootstrapCompatAppProject() {
        writeSettingsGradle("app")
        writeCompatAppBuildFile(buildFile)
    }

    fun writeCompatAppBuildFile(output: File) = output.writeText(
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

    fun writeAppKt(main: String) {
        val sourceDir = projectDir.resolve("src/main/kotlin/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.kt").writeText(
            """
            package com.example.demo
            
            class App
            
            fun main() {
                $main
            }
            
            """.trimIndent()
        )
    }

    fun bootstrapCompatLibProject() {
        writeSettingsGradle("lib")
        writeCompatLibBuildFile(buildFile)
    }

    fun writeCompatLibBuildFile(output: File) = output.writeText(
        """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle-lib") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"
            
            kradle {
                targetJvm("11")
            }
            
        """.trimIndent()
    )

    fun writeSettingsGradle(name: String) {
        settingsFile.writeText(
            """
            rootProject.name = "$name"
            
            """.trimIndent()
        )
    }

    fun writeMultiProjectSettingsGradle(rootProject: String, projects: Collection<String>) {
        settingsFile.writeText(
            """
            rootProject.name = "$rootProject"
            include(${projects.joinToString(" ", "\"", "\"")})
            
            """.trimIndent()
        )
    }

    fun gitInit() {
        val git = Git.init().setDirectory(projectDir).call()
        git.add().addFilepattern(".").call()
        git.commit().setMessage("Initial commit").call()
    }
}
