package net.bnb1.kradle.blueprints

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec

class DevelopmentModeBlueprintTests : CompatSpec({

    test("Check DEV_MODE environment variable") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"DEV_MODE=\" + System.getenv()[\"DEV_MODE\"])")

        val result = runTask("dev")
        result.output shouldContain "DEV_MODE=true"
    }

    test("Run 'dev'") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")

        val result = runTask("dev")
        result.output shouldContain "DEBUG Project root: ${projectDir.absolutePath}" // Agent output
        result.output shouldContain "Hello World"
    }
})
