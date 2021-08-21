package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginSpec

class KtlintBlueprintTests : PluginSpec({

    test("Run ktlint") {
        bootstrapAppProject()

        runTask("lint")

        buildDir.resolve("reports/ktlint/").exists()
    }
})