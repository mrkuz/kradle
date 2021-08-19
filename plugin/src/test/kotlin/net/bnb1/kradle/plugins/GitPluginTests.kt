package net.bnb1.kradle.plugins

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class GitPluginTests : PluginSpec({

    test("Not a Git repository") {
        bootstrapAppProject()
        addTask("testTask", "println(\"Result: \${project.properties[\"gitCommit\"]}\")")

        val result = runTask("testTask")

        result.output shouldContain "Result: null"
    }

    test("Git commit id property is set") {
        bootstrapAppProject()
        gitInit()
        addTask("testTask", "println(\"Result: \${project.properties[\"gitCommit\"]}\")")

        val result = runTask("testTask")

        result.output shouldContain Regex("Result: [a-z0-9]{7}")
    }
})