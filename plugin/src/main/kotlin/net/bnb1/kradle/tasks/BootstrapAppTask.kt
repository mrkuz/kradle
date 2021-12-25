package net.bnb1.kradle.tasks

import net.bnb1.kradle.empty
import org.gradle.api.tasks.Input
import java.nio.file.Path

open class BootstrapAppTask : BootstrapBaseTask() {

    @Input
    val mainClass = project.objects.empty<String>()

    override fun stageTwo() {
        val packageName = mainClass.get().replace(Regex(".[^.]+$"), "")
        val path = Path.of(mainClass.get().replace(".", "/"))
        val mainClassName = path.last().toString().replace(Regex("Kt$"), "")
        val packagePath = path.parent.toString()

        listOf("main", "test").forEach {
            project.projectDir.resolve("src/$it/kotlin/$packagePath").mkdirs()
        }

        project.projectDir.resolve("src/main/kotlin/$packagePath/$mainClassName.kt").writeText(
            """
            package $packageName
            
            class $mainClassName
            
            fun main() {
                println("Hello World!")
            }
            
            """.trimIndent()
        )
    }
}
