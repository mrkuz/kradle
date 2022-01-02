package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class JavaBlueprintTests : PluginSpec({

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
    xtest("Fail using preview features without flag") {
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

        val ex = shouldThrow<UnexpectedBuildFailure> { runTask("compileJava") }

        ex.message shouldContain "use --enable-preview to enable records"
    }

    // Requires JVM 15
    xtest("Use preview features") {
        bootstrapProject {
            """
            jvm {
                targetJvm("15")
                application {
                    mainClass("com.example.demo.App")
                }
                java {
                    withPreviewFeatures(true)
                }
            }
            """.trimIndent()
        }
        createAppJava()

        val result = runTask("compileJava")

        result.task(":compileJava")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})
