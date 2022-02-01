package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class PmdBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """
        }
        project.writeHelloWorldAppJava()

        When("Check for tasks") {

            Then("Task pmdMain is available") {
                project.shouldHaveTask("pmdMain")
            }
        }

        When("Run analyzeCode") {
            val result = project.runTask("analyzeCode")

            Then("pmdMain is called") {
                result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run pmdMain") {
            val result = project.runTask("pmdMain")

            Then("Succeed") {
                result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/pmd/main.html").shouldExist()
            }
        }

        When("Check dependencies") {

            Then("PMD is available") {
                project.shouldHaveDependency("kradlePmd", "net.sourceforge.pmd:pmd-java")
            }
        }
    }

    Given("pmd.version = 6.40.0") {
        project.setUp {
            """
            jvm {
                java {
                   codeAnalysis {
                       pmd {
                           version("6.40.0")
                       }
                   }
                }
                codeAnalysis.enable()
            }
            """
        }
        project.writeHelloWorldAppKt()

        When("Check dependencies") {

            Then("Specified PMD version is used") {
                project.shouldHaveDependency("kradlePmd", "net.sourceforge.pmd:pmd-java:6.40.0")
            }
        }
    }

    Given("Source file with unconditional if-statement") {
        project.writeAppJava { "if (true) return;" }

        And("Default configuration") {
            project.setUp {
                """
                jvm {
                    java.enable()
                    codeAnalysis.enable()
                }                
                """
            }

            When("Run pmdMain") {
                val ex = shouldThrow<UnexpectedBuildFailure> { project.runTask("pmdMain") }

                Then("Fail") {
                    ex.message shouldContain "UnconditionalIfStatement"
                }
            }
        }

        And("codeAnalysis.ignoreFailures = true") {
            project.setUp {
                """
                jvm {
                    java.enable()
                    codeAnalysis {
                        ignoreFailures(true)
                    }
                }                
                """
            }

            When("Run pmdMain") {
                val result = project.runTask("pmdMain")

                Then("Succeed") {
                    result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }

        And("pmd.ruleSets.errorProne(false)") {
            project.setUp {
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
                """
            }

            When("Run pmdMain") {
                val result = project.runTask("pmdMain")

                Then("Succeed") {
                    result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }
    }
})
