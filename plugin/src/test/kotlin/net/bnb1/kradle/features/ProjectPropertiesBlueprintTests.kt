package net.bnb1.kradle.features

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import net.bnb1.kradle.plugins.ProjectPropertiesPlugin

class ProjectPropertiesBlueprintTests : PluginSpec({

    test("Project properties plugin is applied") {
        bootstrapProject {
            """
            general {
                projectProperties.enable()
            }
            """.trimIndent()
        }
        addHasPluginTask(ProjectPropertiesPlugin::class)

        val result = runTask("hasPlugin")

        result.output shouldContain "hasPlugin: true"
    }
})
