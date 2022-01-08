package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class KtlintBlueprintTests : IntegrationSpec({

    test("Fail with no-semi rule") {
        bootstrapProject {
            """
            jvm {
                kotlin {
                    lint {
                        ktlint {
                            rules {
                                enable("no-semi")
                            }
                        }
                    }
                }
                lint.enable()
            }
            """.trimIndent()
        }
        writeAppKt("println(\"Hello World!\");")

        val ex = shouldThrow<UnexpectedBuildFailure> { runTask("ktlintMainSourceSetCheck") }

        ex.message shouldContain "Unnecessary semicolon"
    }

    test("Succeed without error-prone rule set") {
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
        writeAppKt("println(\"Hello World!\");")

        val result = runTask("ktlintMainSourceSetCheck")

        result.task(":ktlintMainSourceSetCheck")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})
