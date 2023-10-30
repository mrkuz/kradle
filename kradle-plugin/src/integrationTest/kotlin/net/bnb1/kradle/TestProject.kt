package net.bnb1.kradle

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.Spec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.eclipse.jgit.api.Git
import org.gradle.api.Plugin
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.nio.file.Files.createTempDirectory
import kotlin.reflect.KClass

class TestProject(spec: Spec) {

    val projectDir: File = createTempDirectory("kradle-test-").toFile()
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
            println("Project dir: ${projectDir.canonicalPath}")
        }
        spec.afterSpec { projectDir.deleteRecursively() }
    }

    fun runTask(task: String, vararg arguments: String): BuildResult = GradleRunner.create()
        .withGradleVersion(Catalog.Versions.gradleForTesting)
        .withProjectDir(projectDir)
        .withPluginClasspath()
        .withArguments(listOf(task) + arguments + listOf("--stacktrace"))
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

    fun shouldHaveDependency(configuration: String, id: String) =
        runTask("dependencies", "--configuration", configuration).output.shouldContain(id)

    fun shouldNotHaveDependency(configuration: String, id: String) =
        runTask("dependencies", "--configuration", configuration).output.shouldNotContain(id)

    fun setUp(name: String = "test", kradleConfig: () -> String) {
        writeSettingsFile(name)
        writeBuildFile(buildFile, kradleConfig)
    }

    fun writeSettingsFile(projectName: String = "test") {
        settingsFile.writeText(
            """
            rootProject.name = "$projectName"
            
            """.trimIndent()
        )
    }

    fun writeBuildFile(kradleConfig: () -> String) = writeBuildFile(buildFile, kradleConfig)

    fun writeBuildFile(output: File, kradleConfig: () -> String) = output.writeText(
        """
        plugins {
            id("org.jetbrains.kotlin.jvm") version "${Catalog.Versions.kotlin}"
            id("net.bitsandbobs.kradle")
        }
        
        group = "com.example"
        version = "1.0.0"
        
        kradle {
            jvm.configureOnly {
                targetJvm("17")
            }
        
        """.trimIndent() + kradleConfig().prependIndent("    ") + "\n}\n"
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

    fun writeHelloWorldAppKt() = writeAppKt { "println(\"Hello World!\")" }

    fun writeAppKt(main: () -> String) {
        val sourceDir = projectDir.resolve("src/main/kotlin/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.kt").writeText(
            """
            package com.example.demo
            
            class App
            
            fun main() {
                ${main()}
            }
            
            """.trimIndent()
        )
    }

    fun writeHelloWorldAppJava() = writeAppJava { "System.out.println(\"Hello World\");" }

    fun writeAppJava(main: () -> String) {
        val sourceDir = projectDir.resolve("src/main/java/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.java").writeText(
            """
            package com.example.demo;
            
            public class App {
            
                public static void main(String[] args) {
                    ${main()}
                }
            }
            """.trimIndent()
        )
    }

    fun gitInit() {
        val git = Git.init().setDirectory(projectDir).call()
        git.add().addFilepattern(".").call()
        git.commit().setMessage("Initial commit").call()
    }

    fun shouldNotHaveProperty(name: String) {
        val taskName = createHasPropertyTask(name)
        runTask(taskName).output.shouldContain("$name=null")
    }

    fun shouldHaveProperty(name: String, value: String) {
        val taskName = createHasPropertyTask(name)
        runTask(taskName).output.shouldContain("$name=$value")
    }

    fun shouldHaveProperty(name: String, pattern: Regex) {
        val taskName = createHasPropertyTask(name)
        runTask(taskName).output.lines().find { it.contains(name) }!!.shouldContain(pattern)
    }

    private fun createHasPropertyTask(name: String): String {
        val taskName = "hasProperty${name[0].uppercaseChar() + name.substring(1)}"
        addTask(
            taskName,
            "println(\"$name=\${project.properties[\"$name\"]}\")"
        )
        return taskName
    }
}
