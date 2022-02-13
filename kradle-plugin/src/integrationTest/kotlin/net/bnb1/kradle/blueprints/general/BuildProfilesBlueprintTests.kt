package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject

class BuildProfilesBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                buildProfiles.enable()
            }
            """.trimIndent()
        }

        When("Check for project properties") {

            Then("profile is set") {
                project.shouldHaveProperty("profile", "default")
            }
        }
    }

    Given("buildProfiles.active = test") {
        project.setUp {
            """
            general {
                buildProfiles {
                    active("test")
                }
            }
            """.trimIndent()
        }

        When("Check for project properties") {

            Then("profile is set to test") {
                project.shouldHaveProperty("profile", "test")
            }
        }
    }

    Given("buildProfiles.active = #gitBranch") {
        project.setUp {
            """
            general {
                git.enable()                
                buildProfiles {
                    active("$#{project.gitBranch}")
                }
            }
            """.trimIndent()
        }
        project.gitInit()

        When("Check for project properties") {

            Then("profile is set to main") {
                project.shouldHaveProperty("profile", "main")
            }
        }
    }
})
