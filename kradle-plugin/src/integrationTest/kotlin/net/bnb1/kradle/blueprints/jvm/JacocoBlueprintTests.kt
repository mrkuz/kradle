package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
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

        Then("Task jacocoTestReport is available") {
            project.shouldHaveTask("jacocoTestReport")
        }

        When("Run jacocoTestReport") {
            val result = project.runTask("jacocoTestReport")

            Then("Succeed") {
                result.task(":jacocoTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/jacoco/test/index.html").shouldExist()
            }

            Then("test is called") {
                result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run test") {
            val result = project.runTask("test")

            Then("jacocoTestReport is called") {
                result.task(":jacocoTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("test.withIntegrationTests = true && jacoco.includes = integrationTest") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter()
                    integrationTests(true)
                    jacoco {
                        includes("integrationTest")
                    }
                }
            }
            """.trimIndent()
        }
        createAppTest("integrationTest")

        When("Check for tasks") {

            Then("Task jacocoIntegrationTestReport is available") {
                project.shouldHaveTask("jacocoIntegrationTestReport")
            }
        }

        When("Run jacocoIntegrationTestReport") {
            val result = project.runTask("jacocoIntegrationTestReport")

            Then("Succeed") {
                result.task(":jacocoIntegrationTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/jacoco/integrationTest/index.html").shouldExist()
            }

            Then("integrationTest is called") {
                result.task(":integrationTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run integrationTest") {
            val result = project.runTask("integrationTest")

            Then("jacocoIntegrationTestReport is called") {
                result.task(":jacocoIntegrationTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
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
