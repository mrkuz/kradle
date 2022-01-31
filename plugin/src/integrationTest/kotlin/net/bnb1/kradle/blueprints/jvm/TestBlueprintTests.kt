package net.bnb1.kradle.blueprints.jvm

import com.adarshr.gradle.testlogger.TestLoggerPlugin
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class TestBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    fun createAppTest(sourceSet: String) {
        val sourceDir = project.projectDir.resolve("src/$sourceSet/kotlin/com/example")
        sourceDir.mkdirs()
        sourceDir.resolve("AppTest.kt").writeText(
            """
            package com.example
            
            import org.junit.jupiter.api.Test
            
            class AppTest {

                @Test
                fun doNothing() {
                    println("Hello Test!")
                }
            }
            """.trimIndent()
        )
    }

    Given("test.withJunitJupiter()") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    withJunitJupiter()
                }
            }
            """.trimIndent()
        }
        createAppTest("test")

        When("Run test") {
            val result = project.runTask("test")

            Then("Succeed") {
                result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Hide stdout") {
                result.output shouldNotContain "Hello Test!"
            }
        }
    }

    Given("test.withIntegrationTests = true") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    withJunitJupiter()
                    withIntegrationTests(true)
                }
            }
            """.trimIndent()
        }
        createAppTest("integrationTest")

        When("Run integrationTest") {
            val result = project.runTask("integrationTest")

            Then("Succeed") {
                result.task(":integrationTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("test.withFunctionalTests = true") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    withJunitJupiter()
                    withFunctionalTests(true)
                }
            }
            """.trimIndent()
        }
        createAppTest("functionalTest")

        When("Run functionalTest") {
            val result = project.runTask("functionalTest")

            Then("Succeed") {
                result.task(":functionalTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("test.customTests(custom)") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    withJunitJupiter()
                    customTests("custom")
                }
            }
            """.trimIndent()
        }
        createAppTest("customTest")

        When("Run customTest") {
            val result = project.runTask("customTest")

            Then("Succeed") {
                result.task(":customTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("test.prettyPrint = true") {
        project.setUp {
            """
           jvm {
               kotlin.enable()
               test {
                   prettyPrint(true)
               }
           }
            """.trimIndent()
        }

        When("Check for plugins") {

            Then("Test logger plugin is applied") {
                project.shouldHavePlugin(TestLoggerPlugin::class)
            }
        }

        When("Run test") {
            val result = project.runTask("test")

            Then("Hide stdout") {
                result.output shouldNotContain "Hello Test!"
            }
        }
    }

    Given("test.standardStreams = true") {
        project.setUp {
            """
           jvm {
               kotlin.enable()
               test {
                   standardStreams(true)
               }
           }
            """.trimIndent()
        }

        When("Run test") {
            val result = project.runTask("test")

            Then("Show stdout") {
                result.output shouldNotContain "Hello Test!"
            }
        }
    }

    Given("test.standardStreams = true && test.prettyPrint = true") {
        project.setUp {
            """
           jvm {
               kotlin.enable()
               test {
                   prettyPrint(true)
                   standardStreams(true)
               }
           }
            """.trimIndent()
        }

        When("Run test") {
            val result = project.runTask("test")

            Then("Show stdout") {
                result.output shouldNotContain "Hello Test!"
            }
        }
    }
})
