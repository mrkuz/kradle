package net.bnb1.kradle.features

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

class MavenPublishBlueprintTests : PluginSpec({

    test("Check 'install' alias") {
        bootstrapProject {
            """
            jvm {
                library.enable()
            }
            """.trimIndent()
        }

        val result = runTask("tasks")

        result.output shouldContain "install "
    }

    test("Maven plugin is enabled") {
        bootstrapProject {
            """
            jvm {
                library.enable()
            }
            """.trimIndent()
        }
        addHasPluginTask(MavenPublishPlugin::class)

        val result = runTask("hasPlugin")

        result.output shouldContain "hasPlugin: true"
    }
})
