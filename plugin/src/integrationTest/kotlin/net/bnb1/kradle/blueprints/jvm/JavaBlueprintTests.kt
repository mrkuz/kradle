package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class JavaBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    fun createAppJava() {
        val sourceDir = project.projectDir.resolve("src/main/java/com/example/demo")
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

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                java.enable()
            }
            """.trimIndent()
        }

        When("Check for plugins") {

            Then("Java plugin is applied") {
                project.shouldHavePlugin(JavaPlugin::class)
            }
        }
    }

    Given("jvm.targetJvm = 7") {
        project.setUp {
            """
            jvm {
                targetJvm("7")
                java.enable()
            }
            """.trimIndent()
        }
        createAppJava()

        When("Run any task") {
            val ex = shouldThrow<UnexpectedBuildFailure> { project.runTask("tasks") }

            Then("Fail") {
                ex.message shouldContain "Minimum supported JVM version is 8"
            }
        }
    }

    Given("jvm.targetJvm > toolchain.languageVersion") {
        project.writeSettingsFile()
        project.buildFile.writeText(
            """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle")
            }
            
            group = "com.example"
            version = "1.0.0"
            
            kradle {
                jvm {
                    targetJvm("16")
                    java.enable()
                }
            }
            
            java {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(11))
                }
            }
            """.trimIndent()
        )

        When("Run any task") {
            val ex = shouldThrow<UnexpectedBuildFailure> { project.runTask("tasks") }

            Then("Fail") {
                ex.message shouldContain "'targetJvm' must be ≤ toolchain language version"
            }
        }
    }

    // Requires JVM 15
    xGiven("jvm.targetJvm = 15 AND java.previewFeatures = false") {
        project.setUp {
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

        When("Run compileJava") {
            val ex = shouldThrow<UnexpectedBuildFailure> { project.runTask("compileJava") }

            Then("Fail") {
                ex.message shouldContain "use --enable-preview to enable records"
            }
        }
    }

    // Requires JVM 15
    xGiven("jvm.targetJvm = 15 AND java.previewFeatures = true") {
        project.setUp {
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

        When("Run compileJava") {
            val result = project.runTask("compileJava")

            Then("Succeed") {
                result.task(":compileJava")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
