package net.bnb1.kradle.blueprints

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class KtlintBlueprintTests : PluginSpec({

    test("Run ktlint") {
        bootstrapAppProject()

        runTask("lint")

        buildDir.resolve("reports/ktlint/").exists()
    }

    test("Check ktlint dependencies") {
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
                ktlintVersion("0.42.0")
            }
            """.trimIndent()
        )

        val result = runTask("dependencies", "--configuration", "ktlint")

        result.output shouldContain "com.pinterest:ktlint"
    }
})