package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
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
                    kover()
                }
            }
            """.trimIndent()
        }
        createAppTest("test")

        When("Check for plugins") {

            Then("Kover plugin is applied") {
                project.shouldHavePlugin(KoverPlugin::class)
            }
        }

        Then("Task koverHtmlReport is available") {
            project.shouldHaveTask("koverHtmlReport")
        }

        When("Run koverHtmlReport") {
            val result = project.runTask("koverHtmlReport")

            Then("Succeed") {
                result.task(":koverHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/kover/project-html/index.html").shouldExist()
            }

            Then("test is called") {
                result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run test") {
            val result = project.runTask("test")

            Then("koverHtmlReport is called") {
                result.task(":koverHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }

    Given("test.withIntegrationTests = true && kover.includes = integrationTest") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                test {
                    junitJupiter()
                    integrationTests(true)
                    kover {
                        includes("integrationTest")
                    }
                }
            }
            """.trimIndent()
        }
        createAppTest("integrationTest")

        When("Run koverHtmlReport") {
            val result = project.runTask("koverHtmlReport")

            Then("Succeed") {
                result.task(":koverHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is generated") {
                project.buildDir.resolve("reports/kover/project-html/index.html").shouldExist()
            }

            Then("integrationTest is called") {
                result.task(":integrationTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run integrationTest") {
            val result = project.runTask("integrationTest")

            Then("koverHtmlReport is called") {
                result.task(":koverHtmlReport")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
