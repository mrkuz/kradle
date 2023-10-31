package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

class KtlintBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.writeSettingsFile()
        // Manually write build file to make sure formatting is fine. Otherwise, ktlint will complain.
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
                    targetJvm("${Catalog.Versions.jvm}")
                    kotlin.enable()
                    lint.enable()
                }
            }
            
            """.trimIndent()
        )
        project.writeHelloWorldAppKt()

        When("Check for plugins") {

            Then("ktlint plugin is applied") {
                project.shouldHavePlugin(KtlintPlugin::class)
            }
        }

        When("Check for tasks") {

            Then("Task ktlintCheck is available") {
                project.shouldHaveTask("ktlintCheck")
            }
        }

        When("Run lint") {
            val result = project.runTask("lint")

            Then("ktlintMainSourceSetCheck is called") {
                result.task(":ktlintMainSourceSetCheck")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run ktlintMainSourceSetCheck") {
            val result = project.runTask("ktlintMainSourceSetCheck")

            Then("Succeed") {
                result.task(":ktlintMainSourceSetCheck")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Report is generated"
                project.buildDir.resolve("reports/ktlint/ktlintMainSourceSetCheck/ktlintMainSourceSetCheck.html")
                    .shouldExist()
            }
        }

        When("Check dependencies") {

            Then("ktlint is available") {
                project.shouldHaveDependency("ktlint", "com.pinterest.ktlint:ktlint-cli")
            }
        }
    }

    Given("ktlint.version = 9.2.0") {
        project.setUp {
            """
            jvm {
                kotlin {
                   lint {
                       ktlint {
                           version("0.41.0")
                       }
                   }
                }
                lint.enable()
            }
            """
        }
        project.writeHelloWorldAppJava()

        When("Check dependencies") {

            Then("Specified ktlint version is used") {
                project.shouldHaveDependency("ktlint", "com.pinterest:ktlint:0.41.0")
            }
        }
    }

    Given("Source file with unnecessary semicolon") {
        project.writeAppKt { "println(\"Hello World!\");" }

        And("Default configuration") {
            project.setUp {
                """
                jvm {
                    kotlin.enable()
                    lint.enable()
                }                
                """
            }

            When("Run ktlintMainSourceSetCheck") {

                Then("Fail") {
                    shouldThrow<UnexpectedBuildFailure> { project.runTask("ktlintMainSourceSetCheck") }
                }
            }
        }

        And("lint.ignoreFailures = true") {
            project.setUp {
                """
                jvm {
                    kotlin.enable()
                    lint {
                        ignoreFailures(true)
                    }
                }                
                """
            }

            When("Run ktlintMainSourceSetCheck") {
                val result = project.runTask("ktlintMainSourceSetCheck")

                Then("Succeed") {
                    result.task(":ktlintMainSourceSetCheck")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }

        And("ktlint.rules.disable(no-semi)") {
            project.setUp {
                """
                jvm {
                    kotlin {
                        lint {
                            ktlint {
                                rules {
                                    disable("no-semi")
                                }
                            }
                        }
                    }
                    lint.enable()
                }
                """
            }

            When("Run ktlintMainSourceSetCheck") {
                val result = project.runTask("ktlintMainSourceSetCheck")

                Then("Succeed") {
                    result.task(":ktlintMainSourceSetCheck")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }

        And("ktlint.rules.disable(ktlint_standard_no-semi)") {
            project.setUp {
                """
                jvm {
                    kotlin {
                        lint {
                            ktlint {
                                rules {
                                    disable("ktlint_standard_no-semi")
                                }
                            }
                        }
                    }
                    lint.enable()
                }
                """
            }

            When("Run ktlintMainSourceSetCheck") {
                val result = project.runTask("ktlintMainSourceSetCheck")

                Then("Succeed") {
                    result.task(":ktlintMainSourceSetCheck")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }
    }
})
