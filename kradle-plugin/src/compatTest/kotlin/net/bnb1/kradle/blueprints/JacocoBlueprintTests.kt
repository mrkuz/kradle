package net.bnb1.kradle.blueprints

import io.kotest.matchers.shouldBe
import net.bnb1.kradle.CompatSpec
import org.gradle.testkit.runner.TaskOutcome

class JacocoBlueprintTests : CompatSpec({

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
        bootstrapCompatAppProject()
        createAppTest("test")

        val result = runTask("test")

        result.task(":jacocoTestReport")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Run tests when generating report") {
        bootstrapCompatAppProject()
        createAppTest("test")

        val result = runTask("jacocoTestReport")

        result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})
