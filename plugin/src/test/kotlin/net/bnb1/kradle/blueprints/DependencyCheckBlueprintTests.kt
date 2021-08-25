package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import net.bnb1.kradle.PluginSpec

class DependencyCheckBlueprintTests : PluginSpec({

    test("Run dependency check") {
        bootstrapAppProject()

        runTask("analyzeDependencies")

        buildDir.resolve("reports/dependency-check-report.html").shouldExist()
    }
})