package net.bnb1.kradle.blueprints

import io.kotest.matchers.shouldBe
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class BuildPropertyBlueprintTests : PluginSpec({

    test("Generate build.properties with 'processResources'") {
        bootstrapAppProject()

        val result = runTask("processResources")

        result.task(":generateBuildProperties")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})