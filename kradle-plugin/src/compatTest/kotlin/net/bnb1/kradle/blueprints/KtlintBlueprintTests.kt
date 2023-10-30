package net.bnb1.kradle.blueprints

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.CompatSpec

class KtlintBlueprintTests : CompatSpec({

    test("Run ktlint") {
        bootstrapCompatAppProject()

        runTask("lint")

        buildDir.resolve("reports/ktlint/").exists()
    }

    test("Check ktlint dependencies") {
        writeSettingsGradle("lib")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "${Catalog.Versions.kotlin}"
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
