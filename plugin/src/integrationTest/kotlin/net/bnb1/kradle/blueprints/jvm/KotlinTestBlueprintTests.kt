package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject

class KotlinTestBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("kotlinTest.useKotest()") {
        project.setUp {
            """
            jvm {
                kotlin {
                    test {
                        useKotest()
                    }
                }
                test.enable()
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("kotest-assertions-core is available") {
                project.shouldHaveDependency("testImplementation", "io.kotest:kotest-assertions-core")
            }

            Then("kotest-runner-junit4 is available") {
                project.shouldHaveDependency("testImplementation", "io.kotest:kotest-runner-junit4")
            }
        }
    }

    Given("kotlinTest.useKotest() AND test.withJunitJupiter()") {
        project.setUp {
            """
            jvm {
                kotlin {
                    test {
                        useKotest()
                    }
                }
                test {
                    withJunitJupiter()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("kotest-assertions-core is available") {
                project.shouldHaveDependency("testImplementation", "io.kotest:kotest-assertions-core")
            }

            Then("kotest-runner-junit4 is available") {
                project.shouldHaveDependency("testImplementation", "io.kotest:kotest-runner-junit5")
            }
        }
    }

    Given("kotlinTest.useMockk()") {
        project.setUp {
            """
            jvm {
                kotlin {
                    test {
                        useMockk()
                    }
                }
                test.enable()
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("mockk is available") {
                project.shouldHaveDependency("testImplementation", "io.mockk:mockk")
            }
        }
    }
})
