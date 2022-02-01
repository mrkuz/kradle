package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class DependencyUpdatesBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
           jvm {
               dependencyUpdates.enable()
           }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task showDependencyUpdates is available") {
                project.shouldHaveTask("showDependencyUpdates")
            }
        }

        When("Run showDependencyUpdates") {
            val result = project.runTask("showDependencyUpdates")

            Then("Succeed") {
                result.task(":showDependencyUpdates")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
