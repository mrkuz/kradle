package net.bnb1.kradle.features

import io.kotest.matchers.shouldBe
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome

class TestBlueprintTests : IntegrationSpec({

    fun createAppTest(sourceSet: String) {
        val sourceDir = projectDir.resolve("src/$sourceSet/kotlin/com/example")
        sourceDir.mkdirs()
        sourceDir.resolve("AppTest.kt").writeText(
            """
            package com.example
            
            import org.junit.jupiter.api.Test
            
            class AppTest {

                @Test
                fun doNothing() = Unit
            }
            
            """.trimIndent()
        )
    }

    Given("customTests(custom)") {
        bootstrapProject {
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
            val result = runTask("customTest")

            Then("Succeed") {
                result.task(":customTest")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
