package net.bnb1.kradle

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.Spec
import io.kotest.matchers.string.shouldContain
import org.gradle.api.Plugin
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.nio.file.Files.createTempDirectory
import kotlin.io.path.ExperimentalPathApi
import kotlin.reflect.KClass

class TestProject(spec: Spec) {

    @OptIn(ExperimentalPathApi::class)
    val projectDir = createTempDirectory("kradle-test-").toFile()
    val settingsFile
        get() = projectDir.resolve("settings.gradle.kts")
    val buildFile
        get() = projectDir.resolve("build.gradle.kts")
    val buildDir
        get() = projectDir.resolve("build")

    init {
        spec.isolationMode = IsolationMode.InstancePerLeaf
        spec.beforeSpec {
            projectDir.mkdirs()
            println("Project dir: ${projectDir.absolutePath}")
        }
        spec.afterSpec { projectDir.deleteRecursively() }
    }

    fun runTask(task: String, vararg arguments: String) = GradleRunner.create()
        .withProjectDir(projectDir)
        .withPluginClasspath()
        .withArguments(listOf(task) + arguments)
        .forwardOutput()
        .build()

    fun shouldHaveTask(name: String) = runTask("tasks").output.shouldContain("$name - ")

    fun shouldHavePlugin(klass: KClass<out Plugin<*>>) {
        val name = klass.simpleName
        addTask(
            "hasPlugin$name",
            "println(\"$name:\" + project.plugins.hasPlugin(${klass.java.name}::class))"
        )
        runTask("hasPlugin$name").output.shouldContain("$name:true")
    }

    fun setUp(name: String = "test", kradleConfig: () -> String) {
        writeSettingsFile(name)
        writeBuildFile(buildFile, kradleConfig)
    }

    fun writeSettingsFile(projectName: String) {
        settingsFile.writeText(
            """
            rootProject.name = "$projectName"
            
            """.trimIndent()
        )
    }

    fun writeBuildFile(output: File, kradleConfig: () -> String) = output.writeText(
        """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle")
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
}
