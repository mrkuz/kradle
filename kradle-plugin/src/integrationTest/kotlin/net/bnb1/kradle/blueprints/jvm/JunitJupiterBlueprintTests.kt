package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject

class JunitJupiterBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("junit-jupiter-api is available") {
                project.shouldHaveDependency("testImplementation", "org.junit.jupiter:junit-jupiter-api")
            }

            Then("junit-jupiter-engine is available") {
                project.shouldHaveDependency("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine")
            }
        }
    }

    Given("junitJupiter.version = 5.8.1") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter {
                        version("5.8.1")
                    }
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("Specified junit-jupiter-api version is used") {
                project.shouldHaveDependency("testImplementation", "org.junit.jupiter:junit-jupiter-api:5.8.1")
            }

            Then("Specified junit-jupiter-engine is used") {
                project.shouldHaveDependency("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:5.8.1")
            }
        }
    }
})
