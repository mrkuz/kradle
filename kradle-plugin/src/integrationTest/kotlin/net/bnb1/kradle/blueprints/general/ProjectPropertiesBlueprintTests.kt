package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject
import net.bnb1.kradle.support.plugins.ProjectPropertiesPlugin

class ProjectPropertiesBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
           general {
               projectProperties.enable()
           }
            """.trimIndent()
        }
        project.projectDir.resolve("project.properties").writeText(
            """
            key = value
            """.trimIndent()
        )

        When("Check for plugins") {

            Then("Project properties plugin is applied") {
                project.shouldHavePlugin(ProjectPropertiesPlugin::class)
            }
        }

        When("Check for project properties") {

            Then("Succeed") {
                project.shouldHaveProperty("key", "value")
            }
        }
    }
})
