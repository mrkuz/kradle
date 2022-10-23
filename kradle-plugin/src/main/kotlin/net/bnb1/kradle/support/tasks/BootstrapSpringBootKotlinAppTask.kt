package net.bnb1.kradle.support.tasks

import net.bnb1.kradle.empty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path

open class BootstrapSpringBootKotlinAppTask : BootstrapBaseTask() {

    @get:Input
    val mainClass = project.objects.empty<String>()

    @TaskAction
    fun run() {
        createDirectories("kotlin")
        createFiles()

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
                
                import org.springframework.boot.CommandLineRunner
                import org.springframework.boot.autoconfigure.SpringBootApplication
                import org.springframework.boot.runApplication
                
                @SpringBootApplication
                class $mainClassName : CommandLineRunner {
                
                    override fun run(args: Array<String>) {
                        println("Hello World!")
                    }
                }

                @Suppress("SpreadOperator")
                fun main(args: Array<String>) {
                    runApplication<$mainClassName>(*args)
                }
                
                """.trimIndent()
            )
        }

        val applicationProperties = project.projectDir.resolve("src/main/resources/application.properties")
        if (!applicationProperties.exists()) {
            applicationProperties.createNewFile()
        }
    }
}
