package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class LintBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                lint.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task lint is available") {
                project.shouldHaveTask("lint")
            }
        }

        When("Run check") {
            val result = project.runTask("check")

            Then("lint is called") {
                result.task(":lint")!!.outcome shouldBe TaskOutcome.UP_TO_DATE
            }
        }
    }
})
