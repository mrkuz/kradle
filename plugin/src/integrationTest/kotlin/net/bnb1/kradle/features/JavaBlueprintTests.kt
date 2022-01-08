package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class JavaBlueprintTests : IntegrationSpec({

    fun createAppJava() {
        val sourceDir = projectDir.resolve("src/main/java/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.java").writeText(
            """
            package com.example.demo;
            
            record Point (int x, int y) {
            }
            
            public class App {
            
                public static void main(String[] args) {
                }
            }
            """.trimIndent()
        )
    }

    // Requires JVM 15
    xGiven("Default configuration") {
        bootstrapProject {
            """
            jvm {
                targetJvm("15")
                application {
                    mainClass("com.example.demo.App")
                }
                java.enable()
            }
            """.trimIndent()
        }
        createAppJava()

        When("Compile code with preview features") {
            val ex = shouldThrow<UnexpectedBuildFailure> { runTask("compileJava") }

            Then("Fail") {
                ex.message shouldContain "use --enable-preview to enable records"
            }
        }
    }

    // Requires JVM 15
    xGiven("previewFeatures(true)") {
        bootstrapProject {
            """
            jvm {
                targetJvm("15")
                application {
                    mainClass("com.example.demo.App")
                }
                java {
                    previewFeatures(true)
                }
            }
            """.trimIndent()
        }
        createAppJava()

        When("Compile code with preview features") {
            val result = runTask("compileJava")

            Then("Succeed") {
                result.task(":compileJava")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
