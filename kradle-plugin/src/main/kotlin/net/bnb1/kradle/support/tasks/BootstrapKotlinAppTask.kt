package net.bnb1.kradle.support.tasks

import net.bnb1.kradle.empty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path

open class BootstrapKotlinAppTask : BootstrapBaseTask() {

    @get:Input
    val mainClass = project.objects.empty<String>()

    @TaskAction
    fun run() {
        val git = initializeGit()
        createDirectories("kotlin")
        createFiles()
        copyTextResource("detekt-config.yml")

        val packageName = mainClass.get().replace(Regex(".[^.]+$"), "")
        val path = Path.of(mainClass.get().replace(".", "/"))
        val mainClassName = path.last().toString().replace(Regex("Kt$"), "")
        val packagePath = path.parent.toString()

        listOf("main", "test").forEach {
            project.projectDir.resolve("src/$it/kotlin/$packagePath").mkdirs()
        }

        val appSource = project.projectDir.resolve("src/main/kotlin/$packagePath/$mainClassName.kt")
        if (!appSource.exists()) {
            appSource.writeText(
                """
                package $packageName
                
                class $mainClassName
                
                fun main() {
                    println("Hello World!")
                }
                
                """.trimIndent()
            )
        }
        git.add().addFilepattern(".").call()
    }
}
