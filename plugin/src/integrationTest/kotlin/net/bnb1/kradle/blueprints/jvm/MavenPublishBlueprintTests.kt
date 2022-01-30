package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.testkit.runner.TaskOutcome

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

        When("Check for plugins") {

            Then("Maven publish plugin is applied") {
                project.shouldHavePlugin(MavenPublishPlugin::class)
            }
        }

        When("Check for tasks") {

            Then("Task install is available") {
                project.shouldHaveTask("install")
            }
        }

        When("Run install") {
            val result = project.runTask("install")

            Then("Succeed") {
                result.task(":install")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
