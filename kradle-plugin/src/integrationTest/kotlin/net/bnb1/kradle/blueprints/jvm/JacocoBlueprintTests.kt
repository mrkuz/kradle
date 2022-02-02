package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testkit.runner.TaskOutcome

class JacocoBlueprintTests : BehaviorSpec({

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
                fun doNothing() {}
            }
            """.trimIndent()
        )
    }

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    withJunitJupiter()
                    withJacoco()
                }
            }
            """.trimIndent()
        }
        createAppTest("test")

        When("Check for plugins") {

            Then("Jacoco plugin is applied") {
                project.shouldHavePlugin(JacocoPlugin::class)
            }
        }

        When("Check dependencies") {

            Then("Jacoco is available") {
                project.shouldHaveDependency("jacocoAgent", "org.jacoco:org.jacoco.agent")
            }
        }

        Then("Task jacocoHtmlReport is available") {
            project.shouldHaveTask("jacocoHtmlReport")
        }

        When("Run jacocoHtmlReport") {
            val result = project.runTask("jacocoHtmlReport")

            Then("Succeed") {
                result.task(":jacocoHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/jacoco/project-html/index.html").shouldExist()
            }

            Then("test is called") {
                result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run test") {
            project.runTask("test")

            Then("Binary report is generated") {
                project.buildDir.resolve("jacoco/test.exec").shouldExist()
            }
        }
    }

    Given("test.withIntegrationTests = true") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter()
                    integrationTests(true)
                    jacoco()
                }
            }
            """.trimIndent()
        }
        createAppTest("integrationTest")

        When("Run jacocoHtmlReport") {
            val result = project.runTask("jacocoHtmlReport")

            Then("Succeed") {
                result.task(":jacocoHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/jacoco/project-html/index.html").shouldExist()
            }

            Then("test is called") {
                result.task(":test")!!.outcome shouldBe TaskOutcome.NO_SOURCE
            }

            Then("integrationTest is called") {
                result.task(":integrationTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run integrationTest") {
            project.runTask("integrationTest")

            Then("Binary report is generated") {
                project.buildDir.resolve("jacoco/integrationTest.exec").shouldExist()
            }
        }
    }

    Given("test.withIntegrationTests = true AND jacoco.excludes = integrationTest") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter()
                    integrationTests(true)
                    jacoco {
                        excludes("integrationTest")
                    }
                }
            }
            """.trimIndent()
        }
        createAppTest("integrationTest")

        When("Run integrationTest") {
            project.runTask("integrationTest")

            Then("Binary report is not generated") {
                project.buildDir.resolve("jacoco/integrationTest.exec").shouldNotExist()
            }
        }
    }

    Given("jacoco.version = 0.8.6") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    withJunitJupiter()
                    jacoco {
                        version("0.8.6")
                    }
                }
            }
            """.trimIndent()
        }

        When("Check dependencies") {

            Then("Specified Jacoco version is used") {
                project.shouldHaveDependency("jacocoAgent", "org.jacoco:org.jacoco.agent:0.8.6")
            }
        }
    }
})
