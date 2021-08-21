package net.bnb1.kradle.blueprints

import io.kotest.matchers.shouldBe
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class JacocoBlueprintTests : PluginSpec({

    fun createAppTest() {
        val sourceDir = projectDir.resolve("src/test/kotlin/com/example")
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
        createAppTest()

        val result = runTask("test")

        result.task(":jacocoTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Run tests when generating report") {
        bootstrapAppProject()
        createAppTest()

        val result = runTask("jacocoTestReport")

        result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})