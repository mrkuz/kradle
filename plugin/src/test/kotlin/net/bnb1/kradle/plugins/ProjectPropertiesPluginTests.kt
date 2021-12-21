package net.bnb1.kradle.plugins

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class ProjectPropertiesPluginTests : PluginSpec({

    test("Load project.properties") {
        bootstrapCompatAppProject()
        projectDir.resolve("project.properties").writeText(
            """
            newProperty = helloTest
            """.trimIndent()
        )
        addTask("testTask", "println(\"Result: \${project.properties[\"newProperty\"]}\")")

        val result = runTask("testTask")

        result.output shouldContain "Result: helloTest"
    }
})