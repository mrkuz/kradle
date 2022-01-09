package net.bnb1.kradle.blueprints

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class KtlintBlueprintTests : IntegrationSpec({

    Given("Default configuration") {
        bootstrapProject {
            """
            jvm {
                kotlin {
                    lint {
                        ktlint {
                            rules {
                                // enable("no-semi")
                            }
                        }
                    }
                }
                lint.enable()
            }
            """.trimIndent()
        }

        And("Source file with unnecessary semicolon") {
            writeAppKt("println(\"Hello World!\");")

            When("Run ktlint") {
                val ex = shouldThrow<UnexpectedBuildFailure> { runTask("ktlintMainSourceSetCheck") }

                Then("Fail") {
                    ex.message shouldContain "Unnecessary semicolon"
                }
            }
        }
    }

    Given("rules.disable(no-semi)") {
        bootstrapProject {
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
            """.trimIndent()
        }

        And("Source file with unnecessary semicolon") {
            writeAppKt("println(\"Hello World!\");")

            When("Run ktlint") {
                val result = runTask("ktlintMainSourceSetCheck")

                Then("Succeed") {
                    result.task(":ktlintMainSourceSetCheck")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }
    }
})
