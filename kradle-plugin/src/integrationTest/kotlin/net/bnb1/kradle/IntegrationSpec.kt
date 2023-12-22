package net.bnb1.kradle

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.internal.DefaultGradleRunner
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.reflect.KClass

abstract class IntegrationSpec(body: IntegrationSpec.() -> Unit) : BehaviorSpec({}) {

    val projectDir: File = createTempDirectory("kradle-test-").toFile()
    val settingsFile
        get() = projectDir.resolve("settings.gradle.kts")
    val buildFile
        get() = projectDir.resolve("build.gradle.kts")
    val buildDir
        get() = projectDir.resolve("build")

    override fun isolationMode(): IsolationMode? {
        return IsolationMode.InstancePerLeaf
    }

    init {
        beforeSpec {
            projectDir.mkdirs()
            println("Project dir: ${projectDir.canonicalPath}")
        }

        afterSpec { projectDir.deleteRecursively() }

        body()
    }

    fun runTask(task: String, vararg arguments: String): BuildResult = DefaultGradleRunner()
        .withJvmArguments("-Xmx4G", "-XX:MaxMetaspaceSize=512m")
        .withGradleVersion(Catalog.Versions.gradleForIntegrationTests)
        .withProjectDir(projectDir)
        .withPluginClasspath()
        .withArguments(listOf(task) + arguments + listOf("--stacktrace"))
        .forwardOutput()
        .build()

    fun addHasPluginTask(clazz: KClass<*>) {
        addTask(
            "hasPlugin",
            "println(\"hasPlugin: \" + project.plugins.hasPlugin(${clazz.java.name}::class))"
        )
    }

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
                id("org.jetbrains.kotlin.jvm") version "${Catalog.Versions.kotlin}"
                id("net.bitsandbobs.kradle") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"
            
            kradle {
                jvm.configureOnly {
                    targetJvm("${Catalog.Versions.jvm}")
                }
                ${kradleConfig()}
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

    fun writeAppJava(main: String) {
        val sourceDir = projectDir.resolve("src/main/java/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.java").writeText(
            """
            package com.example.demo;
            
            public class App {
            
                public static void main(String[] args) {
                    $main
                }
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
}
