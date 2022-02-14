package net.bnb1.kradle.blueprints.general

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure

class ScriptsBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Script printHelloWorld") {
        project.setUp {
            """
            general {
                scripts {
                    "printHelloWorld" {
                        description("Print hello world")
                        commands(
                            ""${'"'}
                            echo "Hello World"
                            ""${'"'}
                        )
                    }
                }
            }
            """
        }

        When("Check for tasks") {

            Then("Task printHelloWorld is available") {
                project.shouldHaveTask("printHelloWorld")
            }
        }

        When("Run printHelloWorld") {
            val result = project.runTask("printHelloWorld")

            Then("Succeed") {
                result.task(":printHelloWorld")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Print 'Hello World'"
                result.output shouldContain "Hello World"
            }
        }
    }

    Given("Script printHelloWorld with dependency on test") {
        project.setUp {
            """
            general {
                scripts {
                    "printHelloWorld" {
                        description("Print hello world")
                        dependsOn("check")
                        commands(
                            ""${'"'}
                            echo "Hello World"
                            ""${'"'}
                        )
                    }
                }
            }
            """
        }

        When("Run printHelloWorld") {
            val result = project.runTask("printHelloWorld")

            Then("check is called") {
                result.task(":check")!!.outcome shouldBe TaskOutcome.UP_TO_DATE
            }
        }
    }

    Given("Script printHelloWorld without description") {
        project.setUp {
            """
            general {
                scripts {
                    "printHelloWorld" {
                        commands(
                            ""${'"'}
                            echo "Hello World"
                            ""${'"'}
                        )
                    }
                }
            }
            """
        }

        When("Run printHelloWorld") {
            val ex = shouldThrow<UnexpectedBuildFailure> { project.runTask("printHelloWorld") }

            Then("Fail") {
                ex.message shouldContain "Missing description"
            }
        }
    }

    Given("Script printHelloWorld without commands") {
        project.setUp {
            """
            general {
                scripts {
                    "printHelloWorld" {
                        description("Print hello world")
                    }
                }
            }
            """
        }

        When("Run printHelloWorld") {
            val ex = shouldThrow<UnexpectedBuildFailure> { project.runTask("printHelloWorld") }

            Then("Fail") {
                ex.message shouldContain "No commands"
            }
        }
    }

    Given("Script with invalid command") {

        project.setUp {
            """
            general {
                scripts {
                    "invalidCommand" {
                        description("Fail")
                        commands(
                            ""${'"'}
                            invalidCommand
                            ""${'"'}
                        )
                    }
                }
            }
            """
        }

        When("Run invalidCommand") {

            Then("Fail") {
                shouldThrow<UnexpectedBuildFailure> { project.runTask("invalidCommand") }
            }
        }
    }
})
