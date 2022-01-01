package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import net.bnb1.kradle.PluginSpec

class OwaspDependencyCheckBlueprintTests : PluginSpec({

    // Takes very long
    xtest("Run dependency check") {
        bootstrapCompatAppProject()

        runTask("analyzeDependencies")

        buildDir.resolve("reports/dependency-check-report.html").shouldExist()
    }
})
