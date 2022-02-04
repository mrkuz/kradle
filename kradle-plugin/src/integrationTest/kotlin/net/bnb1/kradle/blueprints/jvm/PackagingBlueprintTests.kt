package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class PackagingBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                packaging.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task package is available") {
                project.shouldHaveTask("package")
            }
        }

        When("Run package") {
            val result = project.runTask("package")

            Then("Succeed") {
                result.task(":package")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "jar is called"
                result.task(":jar")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
