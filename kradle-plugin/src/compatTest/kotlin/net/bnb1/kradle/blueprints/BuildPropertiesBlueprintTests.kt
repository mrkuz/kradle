package net.bnb1.kradle.blueprints

import io.kotest.matchers.shouldBe
import net.bnb1.kradle.CompatSpec
import org.gradle.testkit.runner.TaskOutcome

class BuildPropertiesBlueprintTests : CompatSpec({

    test("Generate build.properties with 'processResources'") {
        bootstrapCompatAppProject()

        val result = runTask("processResources")

        result.task(":generateBuildProperties")!!.outcome shouldBe TaskOutcome.SUCCESS
    }
})
