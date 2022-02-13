package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject

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

        When("Check for project properties") {

            Then("Succeed") {
                project.shouldHaveProperty("key", "value")
            }
        }
    }

    Given("Default configuration AND build profiles") {
        project.setUp {
            """
           general {
               buildProfiles {
                   active("test")
               }
               projectProperties.enable()
           }
            """.trimIndent()
        }
        project.projectDir.resolve("project.properties").writeText(
            """
            A = 1
            B = 1
            """.trimIndent()
        )
        project.projectDir.resolve("project-test.properties").writeText(
            """
            B = 2
            C = 2
            """.trimIndent()
        )

        When("Check for project properties") {

            Then("Succeed") {
                project.shouldHaveProperty("A", "1")
                project.shouldHaveProperty("B", "2")
                project.shouldHaveProperty("C", "2")
            }
        }
    }
})
