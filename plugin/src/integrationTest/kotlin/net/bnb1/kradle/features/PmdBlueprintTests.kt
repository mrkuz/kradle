package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class PmdBlueprintTests : IntegrationSpec({

    Given("Default configuration") {
        bootstrapProject {
            """
            jvm {
                java {
                    codeAnalysis {
                        pmd {
                            ruleSets {
                                // errorProne(true)
                            }
                        }
                    }
                }
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        And("Source file without flaws") {
            writeAppJava("System.out.println(\"Hello World\");")

            When("Run pmd") {
                runTask("pmdMain")

                Then("Report is generated") {
                    buildDir.resolve("reports/pmd/main.html").shouldExist()
                }
            }

            When("Run check") {
                val result = runTask("check")

                Then("pmd should be executed") {
                    result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }

            When("Check dependencies") {
                val result = runTask("dependencies", "--configuration", "kradlePmd")

                Then("pmd should be available") {
                    result.output shouldContain "net.sourceforge.pmd:pmd-java"
                }
            }
        }

        And("Source file with unconditional if-statement") {
            writeAppJava("if (true) return;")

            When("Run pmd") {
                val ex = shouldThrow<UnexpectedBuildFailure> { runTask("pmdMain") }

                Then("Fail") {
                    ex.message shouldContain "UnconditionalIfStatement"
                }
            }
        }
    }

    Given("ruleSets.errorProne(false)") {
        bootstrapProject {
            """
            jvm {
                java {
                    codeAnalysis {
                        pmd {
                            ruleSets {
                                errorProne(false)
                            }
                        }
                    }
                }
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        And("Source file with unconditional if-statement") {
            writeAppJava("if (true) return;")

            When("Run pmd") {
                val result = runTask("pmdMain")

                Then("Succeed") {
                    result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }
    }
})
