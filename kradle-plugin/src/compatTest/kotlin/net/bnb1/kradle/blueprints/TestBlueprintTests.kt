package net.bnb1.kradle.blueprints

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec
import org.gradle.testkit.runner.TaskOutcome

class TestBlueprintTests : CompatSpec({

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

    test("Check JUnit dependencies") {
        bootstrapCompatLibProject()

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "org.junit.jupiter:junit-jupiter-engine"
        result.output shouldContain "org.junit.jupiter:junit-jupiter-api"
    }

    test("Run test") {
        bootstrapCompatAppProject()
        createAppTest("test")

        val result = runTask("test")

        result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Run integration test") {
        bootstrapCompatAppProject()
        createAppTest("integrationTest")

        val result = runTask("integrationTest")

        result.task(":integrationTest")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Run functional test") {
        bootstrapCompatAppProject()
        createAppTest("functionalTest")

        val result = runTask("functionalTest")

        result.task(":functionalTest")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})
