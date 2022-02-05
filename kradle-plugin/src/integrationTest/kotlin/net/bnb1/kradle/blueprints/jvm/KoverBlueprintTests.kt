package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.shouldBe
import kotlinx.kover.KoverPlugin
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class KoverBlueprintTests : BehaviorSpec({

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
                    junitJupiter()
                }
                codeCoverage.enable()
            }
            """.trimIndent()
        }
        createAppTest("test")

        When("Check for plugins") {

            Then("Kover plugin is applied") {
                project.shouldHavePlugin(KoverPlugin::class)
            }
        }

        When("Check for tasks") {

            Then("Task koverHtmlReport is available") {
                project.shouldHaveTask("koverHtmlReport")
            }
        }

        When("Run koverHtmlReport") {
            val result = project.runTask("koverHtmlReport")

            Then("Succeed") {
                result.task(":koverHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Report is generated"
                project.buildDir.resolve("reports/kover/project-html/index.html").shouldExist()

                // And: "test is called"
                result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run test") {
            project.runTask("test")

            Then("Binary report is generated") {
                project.buildDir.resolve("kover/test.ic").shouldExist()
            }
        }
    }

    Given("test.integrationTests = true") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter()
                    integrationTests(true)
                }
                codeCoverage.enable()
            }
            """.trimIndent()
        }
        createAppTest("integrationTest")

        When("Run koverHtmlReport") {
            val result = project.runTask("koverHtmlReport")

            Then("Succeed") {
                result.task(":koverHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Report is generated"
                project.buildDir.resolve("reports/kover/project-html/index.html").shouldExist()

                // And: "test is called"
                result.task(":test")!!.outcome shouldBe TaskOutcome.NO_SOURCE

                // And: "integrationTest is called"
                result.task(":integrationTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run integrationTest") {
            project.runTask("integrationTest")

            Then("Binary report is generated") {
                project.buildDir.resolve("kover/integrationTest.ic").shouldExist()
            }
        }
    }

    Given("test.withIntegrationTests = true AND kover.excludes = integrationTest") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter()
                    integrationTests(true)
                }
                codeCoverage {
                    kover {
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
                project.buildDir.resolve("kover/integrationTest.ic").shouldNotExist()
            }
        }
    }
})
