package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class SpotBugsBlueprintTests : BehaviorSpec({

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

            Then("Task spotbugsMain is available") {
                project.shouldHaveTask("spotbugsMain")
            }
        }

        When("Run analyzeCode") {
            val result = project.runTask("analyzeCode")

            Then("spotbugsMain is called") {
                result.task(":spotbugsMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run spotbugsMain") {
            val result = project.runTask("spotbugsMain")

            Then("Succeed") {
                result.task(":spotbugsMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/spotbugs/main.html").shouldExist()
            }
        }

        When("Check dependencies") {

            Then("SpotBugs is available") {
                project.shouldHaveDependency("spotbugs", "com.github.spotbugs:spotbugs")
            }
        }
    }

    Given("spotbugs.version = 4.5.2") {
        project.setUp {
            """
            jvm {
                java {
                   codeAnalysis {
                       spotbugs {
                           version("4.5.2")
                       }
                   }
                }
                codeAnalysis.enable()
            }
            """
        }
        project.writeHelloWorldAppKt()

        When("Check dependencies") {

            Then("Specified SpotBugs version is used") {
                project.shouldHaveDependency("spotbugs", "com.github.spotbugs:spotbugs:4.5.2")
            }
        }
    }

    Given("useFbContrib()") {
        project.setUp {
            """
            jvm {
                java {
                    codeAnalysis {
                        spotBugs {
                            useFbContrib()
                        }
                    }
                }
                codeAnalysis.enable()
            }
            """
        }

        When("Check dependencies") {

            Then("sb-contrib is available") {
                project.shouldHaveDependency("spotbugsPlugins", "com.mebigfatguy.sb-contrib:sb-contrib")
            }
        }
    }

    Given("useFindSecBugs()") {
        project.setUp {
            """
            jvm {
                java {
                    codeAnalysis {
                        spotBugs {
                            useFindSecBugs()
                        }
                    }
                }
                codeAnalysis.enable()
            }
            """
        }

        When("Check dependencies") {

            Then("findsecbugs is available") {
                project.shouldHaveDependency("spotbugsPlugins", "com.h3xstream.findsecbugs:findsecbugs-plugin")
            }
        }
    }

    Given("Flawed source code") {
        // FS: Format string should use %n rather than n (VA_FORMAT_STRING_USES_NEWLINE)
        project.writeAppJava { "System.out.println(String.format(\"Hello World\\n\"));" }

        And("Default configuration") {
            project.setUp {
                """
                jvm {
                    java.enable()
                    codeAnalysis.enable()
                }                
                """
            }

            When("Run spotbugsMain") {

                Then("Fail") {
                    shouldThrow<UnexpectedBuildFailure> { project.runTask("spotbugsMain") }
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

            When("Run spotbugsMain") {
                val result = project.runTask("spotbugsMain")

                Then("Succeed") {
                    result.task(":spotbugsMain")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }
    }
})
