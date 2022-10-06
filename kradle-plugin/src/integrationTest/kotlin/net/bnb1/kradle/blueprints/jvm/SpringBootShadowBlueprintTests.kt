package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import net.bnb1.kradle.execute

class SpringBootShadowBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp("app") {
            """
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
                packaging.enable()
                frameworks {
                    springBoot.enable()
                }
            }
            """.trimIndent()
        }

        val sourceDir = project.projectDir.resolve("src/main/kotlin/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.kt").writeText(
            """
            package com.example.demo
            
            import org.springframework.boot.CommandLineRunner
            import org.springframework.boot.autoconfigure.SpringBootApplication
            import org.springframework.boot.runApplication
            
            @SpringBootApplication
            class App : CommandLineRunner {
            
                override fun run(args : Array<String>) {
                    println("Hello World!");
                }
            }
            
            fun main(args: Array<String>) {
                runApplication<App>(*args)
            }
            """.trimIndent()
        )

        When("Check for tasks") {

            Then("Task uberJar is available") {
                project.shouldHaveTask("uberJar")
            }
        }

        When("Run uberJar") {
            project.runTask("uberJar")

            Then("Jar file is created") {
                val jarFile = project.buildDir.resolve("libs/app-1.0.0-uber.jar")
                jarFile.shouldExist()

                // And: "Jar file is executable"
                "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"
            }
        }
    }
})
