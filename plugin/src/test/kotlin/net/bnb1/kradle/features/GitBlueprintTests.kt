package net.bnb1.kradle.features

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import net.bnb1.kradle.plugins.GitPlugin

class GitBlueprintTests : PluginSpec({

    test("Git plugin is applied") {
        bootstrapProject {
            """
            general {
                git.enable()
            }
            """.trimIndent()
        }
        addHasPluginTask(GitPlugin::class)

        val result = runTask("hasPlugin")

        result.output shouldContain "hasPlugin: true"
    }
})
