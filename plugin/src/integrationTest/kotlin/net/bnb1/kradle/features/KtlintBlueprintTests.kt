package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class KtlintBlueprintTests : PluginSpec({

    test("Run ktlint") {
        bootstrapCompatAppProject()

        runTask("lint")

        buildDir.resolve("reports/ktlint/").exists()
    }

    test("Check ktlint dependencies") {
        writeSettingsGradle("lib")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-lib") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                ktlintVersion("0.42.0")
            }
            """.trimIndent()
        )

        val result = runTask("dependencies", "--configuration", "ktlint")

        result.output shouldContain "com.pinterest:ktlint"
    }

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
