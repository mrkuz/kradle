package net.bnb1.kradle.features

import io.kotest.matchers.shouldBe
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class BuildPropertiesBlueprintTests : PluginSpec({

    test("Generate build.properties with 'processResources'") {
        bootstrapCompatAppProject()

        val result = runTask("processResources")

        result.task(":generateBuildProperties")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})
