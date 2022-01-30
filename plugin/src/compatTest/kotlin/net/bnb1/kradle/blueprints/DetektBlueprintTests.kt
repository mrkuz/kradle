package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec
import org.gradle.testkit.runner.TaskOutcome

class DetektBlueprintTests : CompatSpec({

    test("Run detekt") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("detektMain")

        buildDir.resolve("reports/detekt/main.html").shouldExist()
    }

    test("Generate detekt-config.yml") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("generateDetektConfig")

        projectDir.resolve("detekt-config.yml").shouldExist()
    }

    test("Run detekt with 'check'") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")

        val result = runTask("check")

        result.task(":detektMain")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Check detekt dependencies") {
        writeSettingsGradle("lib")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-lib") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                detektVersion("1.17.0")
            }
            """.trimIndent()
        )

        val result = runTask("dependencies", "--configuration", "kradleDetekt")

        result.output shouldContain "io.gitlab.arturbosch.detekt:detekt-cli"
    }
})
