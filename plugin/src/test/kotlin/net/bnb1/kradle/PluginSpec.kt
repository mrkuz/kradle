package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
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

    init {
        beforeEach {
            projectDir.deleteRecursively()
            projectDir.mkdirs()
            println("Project dir: ${projectDir.absolutePath}")
        }

        afterSpec { projectDir.deleteRecursively() }

        body()
    }

    fun runTask(task: String) = GradleRunner.create()
        .withProjectDir(projectDir)
        .withPluginClasspath()
        .withArguments(task)
        .build()

    fun bootstrapAppProject() {
        writeSettingsGradle("app")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "1.0.0-SNAPSHOT"
            }
            
            group = "com.test"
            version = "1.0.0"
            
            application {
                mainClass.set("com.test.AppKt")
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
            
            group = "com.test"
            version = "1.0.0"
            """.trimIndent()
        )
    }

    private fun writeSettingsGradle(name: String) {
        settingsFile.writeText(
            """
            rootProject.name = "$name"
            """.trimIndent()
        )
    }
}
