package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.TestProject
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class JavaBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

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

        When("Check for tasks") {

            Then("Task compile is available") {
                project.shouldHaveTask("compile")

                // And: "Task verify is available"
                project.shouldHaveTask("verify")
            }
        }

        When("Run compile") {
            val result = project.runTask("compile")

            Then("classes is called") {
                result.task(":classes")!!.outcome shouldBe TaskOutcome.UP_TO_DATE
            }
        }

        When("Run verify") {
            val result = project.runTask("verify")

            Then("check is called") {
                result.task(":check")!!.outcome shouldBe TaskOutcome.UP_TO_DATE
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
        project.writeHelloWorldAppJava()

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
                id("org.jetbrains.kotlin.jvm") version "${Catalog.Versions.kotlin}"
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

    Given("java.useLombok = true") {
        project.setUp {
            """
            jvm {
                java {
                   withLombok()
                }
            }
            """.trimIndent()
        }

        val sourceDir = project.projectDir.resolve("src/main/java/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("Point.java").writeText(
            """
            package com.example.demo;
            
            import lombok.Data;
            
            @Data            
            public class Point {
                private int x;
                private int y;
            }
            """.trimIndent()
        )

        When("Check dependencies") {

            Then("lombok is available") {
                project.shouldHaveDependency("implementation", "org.projectlombok:lombok")

                // And: "annotation processor is available"
                project.shouldHaveDependency("annotationProcessor", "org.projectlombok:lombok")

                // And: "findbugs:annotations is available"
                project.shouldHaveDependency("compileOnly", "com.google.code.findbugs:annotations")
            }
        }

        When("Check for tasks") {

            Then("Task generateLombokConfig is available") {
                project.shouldHaveTask("generateLombokConfig")
            }
        }

        When("Run generateLombokConfig") {
            val result = project.runTask("generateLombokConfig")

            Then("Succeed") {
                result.task(":generateLombokConfig")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "lombok.config is created"
                project.projectDir.resolve("lombok.config").shouldExist()
            }
        }

        When("Run compileJava") {
            val result = project.runTask("compileJava")

            Then("Succeed") {
                result.task(":compileJava")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "generateLombokConfig is called"
                result.task(":generateLombokConfig")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("java.useLombok = true AND bootstrap") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                java {
                   withLombok()
                }
            }
            """.trimIndent()
        }

        When("Run bootstrap") {
            val result = project.runTask("bootstrap")

            Then("generateLombokConfig is called") {
                result.task(":generateLombokConfig")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
