package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject

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
