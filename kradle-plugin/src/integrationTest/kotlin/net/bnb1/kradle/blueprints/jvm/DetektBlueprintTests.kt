package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class DetektBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Check for tasks") {

            Then("Task detektMain is available") {
                project.shouldHaveTask("detektMain")

                // And: "Task generateDetektConfig is available"
                project.shouldHaveTask("generateDetektConfig")
            }
        }

        When("Run analyzeCode") {
            val result = project.runTask("analyzeCode")

            Then("detektMain is called") {
                result.task(":detektMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run detektMain") {
            val result = project.runTask("detektMain")

            Then("Succeed") {
                result.task(":detektMain")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Report is generated"
                project.buildDir.resolve("reports/detekt/main.html").shouldExist()
            }
        }

        When("Run generateDetektConfig") {
            val result = project.runTask("generateDetektConfig")

            Then("Succeed") {
                result.task(":generateDetektConfig")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "detekt-config.yml is generated"
                project.projectDir.resolve("detekt-config.yml").shouldExist()
            }
        }

        When("Check dependencies") {

            Then("detekt is available") {
                project.shouldHaveDependency("kradleDetekt", "io.gitlab.arturbosch.detekt:detekt-cli")
            }
        }
    }

    Given("detekt.version = 1.18.0") {
        project.setUp {
            """
            jvm {
                kotlin {
                   codeAnalysis {
                       detekt {
                           version("1.18.0")
                       }
                   }
                }
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Check dependencies") {

            Then("Specified detekt version is used") {
                project.shouldHaveDependency("kradleDetekt", "io.gitlab.arturbosch.detekt:detekt-cli:1.18.0")
            }
        }
    }

    Given("detekt.configFile = detekt.yml") {
        project.setUp {
            """
            jvm {
                kotlin {
                   codeAnalysis {
                       detekt {
                           configFile("detekt.yml")
                       }
                   }
                }
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Run generateDetektConfig") {
            project.runTask("generateDetektConfig")

            Then("detekt.yml is generated") {
                project.projectDir.resolve("detekt.yml").shouldExist()
            }
        }
    }

    Given("Flawed source code") {
        val sourceDir = project.projectDir.resolve("src/main/kotlin/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("App.kt").writeText(
            """
            package com.example.demo
            
            class Empty {
            }
            """.trimIndent()
        )

        And("Default configuration") {
            project.setUp {
                """
                jvm {
                    kotlin.enable()
                    codeAnalysis.enable()
                }                
                """.trimIndent()
            }

            When("Run detektMain") {

                Then("Fail") {
                    shouldThrow<UnexpectedBuildFailure> { project.runTask("detektMain") }
                }
            }
        }

        And("codeAnalysis.ignoreFailures = true") {
            project.setUp {
                """
                jvm {
                    kotlin.enable()
                    codeAnalysis {
                        ignoreFailures(true)
                    }
                }                
                """.trimIndent()
            }

            When("Run detektMain") {
                val result = project.runTask("detektMain")

                Then("Succeed") {
                    result.task(":detektMain")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }
    }

    Given("Default configuration AND bootstrap") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                kotlin.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        When("Run bootstrap") {
            val result = project.runTask("bootstrap")

            Then("generateDetektConfig is called") {
                result.task(":generateDetektConfig")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
