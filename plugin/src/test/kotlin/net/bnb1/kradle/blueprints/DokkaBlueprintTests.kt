package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class DokkaBlueprintTests : PluginSpec({

    test("Generate documentation") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("generateDocumentation")

        buildDir.resolve("docs/index.html").shouldExist()
    }

    // Raises OutOfMemoryError: Metaspace
    xtest("Generate documentation with package.md") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")
        projectDir.resolve("package.md").writeText(
            """
            # Module app
            
            Hello package.md
            """.trimIndent()
        )

        runTask("generateDocumentation")

        val output = buildDir.resolve("docs/index.html")
        output.shouldExist()
        output.readText() shouldContain "Hello package.md"
    }

    // Raises OutOfMemoryError: Metaspace
    xtest("Generate documentation with module.md") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")
        projectDir.resolve("module.md").writeText(
            """
            # Module app
            
            Hello module.md
            """.trimIndent()
        )

        runTask("generateDocumentation")

        val output = buildDir.resolve("docs/index.html")
        output.shouldExist()
        output.readText() shouldContain "Hello module.md"
    }
})