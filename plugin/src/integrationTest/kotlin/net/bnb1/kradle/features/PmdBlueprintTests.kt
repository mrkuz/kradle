package net.bnb1.kradle.features

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class PmdBlueprintTests : IntegrationSpec({

    test("Run PMD") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        runTask("pmdMain")

        buildDir.resolve("reports/pmd/main.html").shouldExist()
    }

    test("Run PMD with 'check'") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        val result = runTask("check")

        result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Check PMD dependencies") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "kradlePmd")

        result.output shouldContain "net.sourceforge.pmd:pmd-java"
    }

    test("Fail with error-prone rule set") {
        bootstrapProject {
            """
            jvm {
                java {
                    codeAnalysis {
                        pmd {
                            ruleSets {
                                errorProne(true)
                            }
                        }
                    }
                }
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        writeAppJava("if (true) return;")

        val ex = shouldThrow<UnexpectedBuildFailure> { runTask("pmdMain") }

        ex.message shouldContain "UnconditionalIfStatement"
    }

    test("Succeed without error-prone rule set") {
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
        writeAppJava("if (true) return;")

        val result = runTask("pmdMain")

        result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})
