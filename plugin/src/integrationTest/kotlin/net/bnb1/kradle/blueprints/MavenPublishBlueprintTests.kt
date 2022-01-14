package net.bnb1.kradle.blueprints

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

class MavenPublishBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                library.enable()
            }
            """.trimIndent()
        }

        Then("Task install is available") {
            project.shouldHaveTask("install")
        }

        Then("Maven plugin is applied") {
            project.shouldHavePlugin(MavenPublishPlugin::class)
        }
    }
})
