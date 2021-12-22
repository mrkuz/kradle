package net.bnb1.kradle.tasks

import net.bnb1.kradle.features.jvm.ApplicationProperties
import net.bnb1.kradle.propertiesRegistry
import java.nio.file.Path

open class BootstrapAppTask : AbstractBoostrapTask() {

    override fun stageTwo() {
        // TODO: Replace with task property
        val properties = project.propertiesRegistry.get<ApplicationProperties>()
        val packageName = properties.mainClass.get().replace(Regex(".[^.]+$"), "")
        val path = Path.of(properties.mainClass.get().replace(".", "/"))
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
