package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject

class BootstrapBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task bootstrap is available") {
                project.shouldHaveTask("bootstrap")
            }
        }
    }
})
