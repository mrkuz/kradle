package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class DetektBlueprintTests : PluginSpec({

    test("Run detekt") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("analyzeCode")

        buildDir.resolve("reports/detekt/detekt.html").shouldExist()
    }

    test("Generate detekt-config.yml") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("generateDetektConfig")

        projectDir.resolve("detekt-config.yml").shouldExist()
    }

    test("Run detekt with 'check'") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")

        val result = runTask("check")

        result.task(":analyzeCode")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})