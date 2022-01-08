package net.bnb1.kradle.features

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

class MavenPublishBlueprintTests : IntegrationSpec({

    Given("Default configuration") {
        bootstrapProject {
            """
            jvm {
                library.enable()
            }
            """.trimIndent()
        }
        addHasPluginTask(MavenPublishPlugin::class)

        When("List tasks") {
            val result = runTask("tasks")

            Then("install should be available") {
                result.output shouldContain "install "
            }
        }

        When("Check for plugin") {
            val result = runTask("hasPlugin")

            Then("MavenPublishPlugin is applied") {
                result.output shouldContain "hasPlugin: true"
            }
        }
    }
})
