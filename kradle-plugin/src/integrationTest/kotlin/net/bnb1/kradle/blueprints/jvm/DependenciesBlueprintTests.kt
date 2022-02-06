package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class DependenciesBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                dependencies.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task showDependencyUpdates is available") {
                project.shouldHaveTask("showDependencyUpdates")
            }
        }

        When("Run showDependencyUpdates") {
            val result = project.runTask("showDependencyUpdates")

            Then("Succeed") {
                result.task(":showDependencyUpdates")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("useGuava = true") {
        project.setUp {
            """
            jvm {
                dependencies {
                    useGuava()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("guava is available") {
                project.shouldHaveDependency("implementation", "com.google.guava:guava")
            }
        }
    }

    Given("useCaffeine = true") {
        project.setUp {
            """
            jvm {
                dependencies {
                    useCaffeine()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("com.github.ben-manes.caffeine is available") {
                project.shouldHaveDependency("implementation", "com.github.ben-manes.caffeine:caffeine")
            }
        }
    }

    Given("useCaffeine = true AND useGuava = true") {
        project.setUp {
            """
            jvm {
                dependencies {
                    useGuava()
                    useCaffeine()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("caffeine:guava is available") {
                project.shouldHaveDependency("implementation", "com.github.ben-manes.caffeine:guava")
            }
        }
    }

    Given("useLog4j = true") {
        project.setUp {
            """
            jvm {
                dependencies {
                    useLog4j()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("log4j is available") {
                project.shouldHaveDependency("implementation", "org.apache.logging.log4j:log4j")
            }
        }
    }
})
