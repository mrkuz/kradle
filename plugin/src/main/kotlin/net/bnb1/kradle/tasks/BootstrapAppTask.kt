package net.bnb1.kradle.tasks

import org.gradle.api.plugins.JavaApplication
import java.nio.file.Path

open class BootstrapAppTask : AbstractBoostrapTask() {

    override fun stageTwo() {
        val javaExtension = project.extensions.getByType(JavaApplication::class.java)
        val mainClass = javaExtension.mainClass.get();
        val path = Path.of(mainClass.replace(".", "/"))
        val mainClassName = path.last().toString().replace(Regex("Kt$"), "")
        val packagePath = path.parent.toString()

        listOf("main", "test").forEach {
            project.rootDir.resolve("src/$it/kotlin/$packagePath").mkdirs()
        }

        project.rootDir.resolve("src/main/kotlin/$packagePath/${mainClassName}.kt").writeText(
            """
            package com.example
            
            class $mainClassName
            
            fun main() {
                println("Hello World!")
            }
            
            """.trimIndent()
        )
    }
}