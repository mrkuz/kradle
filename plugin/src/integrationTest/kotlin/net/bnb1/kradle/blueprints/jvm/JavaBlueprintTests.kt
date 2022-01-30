package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.api.plugins.JavaPlugin
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
})
