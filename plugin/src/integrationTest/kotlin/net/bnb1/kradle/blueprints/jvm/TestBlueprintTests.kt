package net.bnb1.kradle.blueprints.jvm

import com.adarshr.gradle.testlogger.TestLoggerPlugin
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class TestBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    fun createAppTest(sourceSet: String) {
        val sourceDir = project.projectDir.resolve("src/$sourceSet/kotlin/com/example/demo")
        sourceDir.mkdirs()
        sourceDir.resolve("AppTest.kt").writeText(
            """
            package com.example
            
            import org.junit.jupiter.api.Test
            
            class AppTest {

                @Test
                fun doNothing() {
                    println("Hello Test!")
                    println("PROJECT_DIR = " + System.getenv("PROJECT_DIR"))
                    println("PROJECT_ROOT_DIR = " + System.getenv("PROJECT_ROOT_DIR"))
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
                   withJunitJupiter()
                   prettyPrint(true)
               }
           }
            """.trimIndent()
        }
        createAppTest("test")

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
                   withJunitJupiter()
                   standardStreams(true)
               }
           }
            """.trimIndent()
        }
        createAppTest("test")

        When("Run test") {
            val result = project.runTask("test")

            Then("Show stdout") {
                result.output shouldContain "Hello Test!"
            }

            Then("PROJECT_DIR is set") {
                result.output shouldContain "PROJECT_DIR = " + project.projectDir.absoluteFile
            }

            Then("PROJECT_ROOT_DIR is set") {
                result.output shouldContain "PROJECT_ROOT_DIR = " + project.projectDir.absoluteFile
            }
        }
    }

    Given("test.standardStreams = true && test.prettyPrint = true") {
        project.setUp {
            """
           jvm {
               kotlin.enable()
               test {
                   withJunitJupiter()
                   prettyPrint(true)
                   standardStreams(true)
               }
           }
            """.trimIndent()
        }
        createAppTest("test")

        When("Run test") {
            val result = project.runTask("test")

            Then("Show stdout") {
                result.output shouldContain "Hello Test!"
            }
        }
    }

    Given("test.useArchUnit()") {
        project.setUp {
            """
            jvm {
                test {
                    useArchUnit()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("archunit is available") {
                project.shouldHaveDependency("testImplementation", "com.tngtech.archunit:archunit")
            }
        }
    }

    Given("test.useArchUnit() AND test.junitJupiter()") {
        project.setUp {
            """
            jvm {
                kotlin {
                    test {
                        useKotest()
                    }
                }
                test {
                    useArchUnit()
                    junitJupiter()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("archunit-junit5 is available") {
                project.shouldHaveDependency("testImplementation", "com.tngtech.archunit:archunit-junit5")
            }
        }
    }

    Given("test.useTestcontainers()") {
        project.setUp {
            """
            jvm {
                test {
                    useTestcontainers()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("testcontainers is available") {
                project.shouldHaveDependency("testImplementation", "org.testcontainers:testcontainers")
            }
        }
    }

    Given("test.useTestcontainers() AND test.junitJupiter()") {
        project.setUp {
            """
            jvm {
                kotlin {
                    test {
                        useKotest()
                    }
                }
                test {
                    useTestcontainers()
                    junitJupiter()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("testcontainers is available") {
                project.shouldHaveDependency("testImplementation", "org.testcontainers:testcontainers")
            }

            Then("testcontainers.junit-jupiter is available") {
                project.shouldHaveDependency("testImplementation", "org.testcontainers:junit-jupiter")
            }
        }
    }
})
