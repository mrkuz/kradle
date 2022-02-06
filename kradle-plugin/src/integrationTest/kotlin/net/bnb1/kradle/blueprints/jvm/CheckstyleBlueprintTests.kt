package net.bnb1.kradle.blueprints.jvm

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class CheckstyleBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                java.enable()
                lint.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppJava()

        When("Check for tasks") {

            Then("Task checkstyleMain is available") {
                project.shouldHaveTask("checkstyleMain")

                // And: "Task generateCheckstyleConfig is available"
                project.shouldHaveTask("generateCheckstyleConfig")
            }
        }

        When("Run lint") {
            val result = project.runTask("lint")

            Then("checkstyleMain is called") {
                result.task(":checkstyleMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run checkstyleMain") {
            val result = project.runTask("checkstyleMain")

            Then("Succeed") {
                result.task(":checkstyleMain")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Report is generated"
                project.buildDir.resolve("reports/checkstyle/main.html").shouldExist()
            }
        }

        When("Run generateCheckstyleConfig") {
            val result = project.runTask("generateCheckstyleConfig")

            Then("Succeed") {
                result.task(":generateCheckstyleConfig")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "checkstyle.xml is generated"
                project.projectDir.resolve("checkstyle.xml").shouldExist()
            }
        }

        When("Check dependencies") {

            Then("checkstyle is available") {
                project.shouldHaveDependency("kradleCheckstyle", "om.puppycrawl.tools:checkstyle")
            }
        }
    }

    Given("checkstyle.version = 9.2.0") {
        project.setUp {
            """
            jvm {
                java {
                   lint {
                       checkstyle {
                           version("9.2.0")
                       }
                   }
                }
                lint.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppJava()

        When("Check dependencies") {

            Then("Specified checkstyle version is used") {
                project.shouldHaveDependency("kradleCheckstyle", "om.puppycrawl.tools:checkstyle:9.2.0")
            }
        }
    }

    Given("checkstyle.configFile = checkstyle-config.xml") {
        project.setUp {
            """
            jvm {
                java {
                   lint {
                       checkstyle {
                           configFile("checkstyle-config.xml")
                       }
                   }
                }
                lint.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppJava()

        When("Run generateCheckstyleConfig") {
            project.runTask("generateCheckstyleConfig")

            Then("checkstyle-config.xml is generated") {
                project.projectDir.resolve("checkstyle-config.xml").shouldExist()
            }
        }
    }

    Given("Flawed source code") {
        project.writeAppJava { "        System.out.println(\"Hello World\");" }

        And("Default configuration") {
            project.setUp {
                """
                jvm {
                    java.enable()
                    lint.enable()
                }                
                """.trimIndent()
            }

            When("Run checkstyleMain") {

                Then("Fail") {
                    shouldThrow<UnexpectedBuildFailure> { project.runTask("checkstyleMain") }
                }
            }
        }

        And("lint.ignoreFailures = true") {
            project.setUp {
                """
                jvm {
                    java.enable()
                    lint {
                        ignoreFailures(true)
                    }
                }                
                """.trimIndent()
            }

            When("Run checkstyleMain") {
                val result = project.runTask("checkstyleMain")

                Then("Succeed") {
                    result.task(":checkstyleMain")!!.outcome shouldBe TaskOutcome.SUCCESS
                }
            }
        }
    }
})
