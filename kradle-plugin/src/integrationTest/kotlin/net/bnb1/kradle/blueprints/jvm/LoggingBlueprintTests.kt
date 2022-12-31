package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class LoggingBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("withSlf4j = true") {
        project.setUp {
            """
            jvm {
                logging {
                    withSlf4j()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("slf4j-api is available") {
                project.shouldHaveDependency("implementation", "org.slf4j:slf4j-api")

                // And: slf4j-simple is available
                project.shouldHaveDependency("implementation", "org.slf4j:slf4j-simple")
            }
        }
    }

    Given("withLog4j = true") {
        project.setUp {
            """
            jvm {
                logging {
                    withLog4j()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("log4j-api is available") {
                project.shouldHaveDependency("implementation", "org.apache.logging.log4j:log4j-api")

                // And: log4j-core is available
                project.shouldHaveDependency("implementation", "org.apache.logging.log4j:log4j-core")
            }
        }

        When("Check for tasks") {

            Then("Task generateLog4jConfig is available") {
                project.shouldHaveTask("generateLog4jConfig")
            }
        }

        When("Run generateLog4jConfig") {
            val result = project.runTask("generateLog4jConfig")

            Then("Succeed") {
                result.task(":generateLog4jConfig")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "log4j.xml is created"
                project.projectDir.resolve("src/main/resources/log4j2.xml").shouldExist()
            }
        }
    }

    Given("logging.withLog4j = true AND bootstrap") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                logging {
                    withLog4j()
                }
            }
            """.trimIndent()
        }

        When("Run bootstrap") {
            val result = project.runTask("bootstrap")

            Then("generateLog4jConfig is called") {
                result.task(":generateLog4jConfig")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("withSlf4j = true AND withLog4j = true") {
        project.setUp {
            """
            jvm {
                logging {
                    withSlf4j()
                    withLog4j()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("log4j-api is available") {
                project.shouldHaveDependency("implementation", "org.apache.logging.log4j:log4j-api")

                // And: log4j-core is available
                project.shouldHaveDependency("implementation", "org.apache.logging.log4j:log4j-core")

                // And: log4j-slf4j2-impl is available
                project.shouldHaveDependency("implementation", "org.apache.logging.log4j:log4j-slf4j2-impl")

                // And: slf4j-simple is not available
                project.shouldNotHaveDependency("implementation", "org.slf4j:slf4j-simple")
            }
        }
    }
})
