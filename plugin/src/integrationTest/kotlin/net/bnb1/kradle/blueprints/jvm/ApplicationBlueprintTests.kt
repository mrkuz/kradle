package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class ApplicationBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                application.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Run 'run'") {

            Then("Fail") {
                shouldThrow<UnexpectedBuildFailure> { project.runTask("run") }
            }
        }
    }

    Given("application.mainClass is set") {
        project.setUp {
            """
                jvm {
                    kotlin.enable()
                    application {
                        mainClass("com.example.demo.AppKt")
                    }
                }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Run 'run'") {
            val result = project.runTask("run")

            Then("Succeed") {
                result.task(":run")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Check for plugins") {

            Then("Application plugin is applied") {
                project.shouldHavePlugin(ApplicationPlugin::class)
            }
        }
    }

    Given("Project without group") {
        project.writeSettingsFile()
        project.buildFile.writeText(
            """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle")
            }

            version = "1.0.0"

            kradle {
                jvm {
                    targetJvm("11")
                    kotlin.enable()
                    application {
                        mainClass("com.example.demo.AppKt")
                    }
                }
            }
            """.trimIndent()
        )
        project.writeHelloWorldAppKt()

        When("Run any task") {
            val result = project.runTask("tasks")

            Then("Print warning") {
                result.output shouldContain "WARNING: Group is not specified"
            }
        }
    }

    Given("Project with invalid group") {
        project.writeSettingsFile()
        project.buildFile.writeText(
            """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle")
            }

            group = "com_example"
            version = "1.0.0"

            kradle {
                jvm {
                    targetJvm("11")
                    kotlin.enable()
                    application {
                        mainClass("com.example.demo.AppKt")
                    }
                }
            }
            """.trimIndent()
        )
        project.writeHelloWorldAppKt()

        When("Run any task") {
            val result = project.runTask("tasks")

            Then("Print warning") {
                result.output shouldContain "WARNING: Group doesn't comply with Java's package name rules"
            }
        }
    }

    Given("Project without version") {
        project.writeSettingsFile()
        project.buildFile.writeText(
            """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.6.0"
                id("net.bitsandbobs.kradle")
            }

            group = "com.example"

            kradle {
                jvm {
                    targetJvm("11")
                    kotlin.enable()
                    application {
                        mainClass("com.example.demo.AppKt")
                    }
                }
            }
            """.trimIndent()
        )
        project.writeHelloWorldAppKt()

        When("Run any task") {
            val result = project.runTask("tasks")

            Then("Print warning") {
                result.output shouldContain "WARNING: Version is not specified"
            }
        }
    }

    Given("java.previewFeatures = true") {
        // TODO
    }
})
