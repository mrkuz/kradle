package net.bnb1.kradle.blueprints

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class TestBlueprintTests : PluginSpec({

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
               id("net.bnb1.kradle-lib") version "main-SNAPSHOT"
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
               id("net.bnb1.kradle-lib") version "main-SNAPSHOT"
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
})