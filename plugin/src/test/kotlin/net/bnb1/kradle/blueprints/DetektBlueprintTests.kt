package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import net.bnb1.kradle.PluginSpec

class DetektBlueprintTests : PluginSpec({

    test("Run detekt") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("analyzeCode")

        buildDir.resolve("reports/detekt/detekt.html").shouldExist()
    }
})