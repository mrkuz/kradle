package net.bnb1.kradle.blueprints

import io.kotest.matchers.shouldBe
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class JacocoBlueprintTests : PluginSpec({

    fun createAppTest(sourceSet: String) {
        val sourceDir = projectDir.resolve("src/$sourceSet/kotlin/com/example")
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

    test("Generate report after test") {
        bootstrapAppProject()
        createAppTest("test")

        val result = runTask("test")

        result.task(":jacocoTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Run tests when generating report") {
        bootstrapAppProject()
        createAppTest("test")

        val result = runTask("jacocoTestReport")

        result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Generate report after integration test") {
        bootstrapAppProject()
        createAppTest("integrationTest")

        val result = runTask("integrationTest")

        result.task(":jacocoIntegrationTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Generate report after functional test") {
        bootstrapAppProject()
        createAppTest("functionalTest")

        val result = runTask("functionalTest")

        result.task(":jacocoFunctionalTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})