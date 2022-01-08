package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec

class DokkaBlueprintTests : CompatSpec({

    test("Generate documentation") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("generateDocumentation")

        buildDir.resolve("docs/index.html").shouldExist()
    }

    // Raises OutOfMemoryError: Metaspace
    xtest("Generate documentation with package.md (project dir") {
        bootstrapCompatAppProject()
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
    xtest("Generate documentation with package.md (source dir") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")
        projectDir.resolve("src/main/kotlin/com/example/package.md").writeText(
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
    xtest("Generate documentation with module.md (project dir)") {
        bootstrapCompatAppProject()
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

    // Raises OutOfMemoryError: Metaspace
    xtest("Generate documentation with module.md (source dir)") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")
        projectDir.resolve("src/main/kotlin/com/example/module.md").writeText(
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
