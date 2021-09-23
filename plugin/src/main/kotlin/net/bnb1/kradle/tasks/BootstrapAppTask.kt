package net.bnb1.kradle.tasks

import net.bnb1.kradle.KradleExtension
import java.nio.file.Path

open class BootstrapAppTask : AbstractBoostrapTask() {

    override fun stageTwo() {
        val extension = project.extensions.getByType(KradleExtension::class.java)
        val packageName = extension.mainClass.replace(Regex(".[^.]+$"), "")
        val path = Path.of(extension.mainClass.replace(".", "/"))
        val mainClassName = path.last().toString().replace(Regex("Kt$"), "")
        val packagePath = path.parent.toString()

        listOf("main", "test").forEach {
            project.rootDir.resolve("src/$it/kotlin/$packagePath").mkdirs()
        }

        project.rootDir.resolve("src/main/kotlin/$packagePath/${mainClassName}.kt").writeText(
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