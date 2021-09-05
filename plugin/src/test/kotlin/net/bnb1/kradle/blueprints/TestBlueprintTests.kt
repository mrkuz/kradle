package net.bnb1.kradle.blueprints

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class TestBlueprintTests : PluginSpec({

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

    test("Check JUnit dependencies") {
        bootstrapLibProject()

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "org.junit.jupiter:junit-jupiter-engine:5.7.2"
        result.output shouldContain "org.junit.jupiter:junit-jupiter-api:5.7.2"
    }

    test("Check kotest dependencies") {
        writeSettingsGradle("lib")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bitsandbobs.kradle-lib") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                tests {
                    useKotest()
                }
            }
            """.trimIndent()
        )

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "io.kotest:kotest-runner-junit5:4.6.1"
        result.output shouldContain "io.kotest:kotest-assertions-core:4.6.1"
    }

    test("Check mockk dependencies") {
        writeSettingsGradle("lib")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bitsandbobs.kradle-lib") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                tests {
                    useMockk()
                }
            }
            """.trimIndent()
        )

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "io.mockk:mockk:1.12.0"
    }

    test("Run test") {
        bootstrapAppProject()
        createAppTest("test")

        val result = runTask("test")

        result.task(":test")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Run integration test") {
        bootstrapAppProject()
        createAppTest("integrationTest")

        val result = runTask("integrationTest")

        result.task(":integrationTest")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Run functional test") {
        bootstrapAppProject()
        createAppTest("functionalTest")

        val result = runTask("functionalTest")

        result.task(":functionalTest")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})