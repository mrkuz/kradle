package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class CodeAnalysisBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task analyzeCode is available") {
                project.shouldHaveTask("analyzeCode")
            }
        }

        When("Run check") {
            val result = project.runTask("check")

            Then("analyzeCode is called") {
                result.task(":analyzeCode")!!.outcome shouldBe TaskOutcome.UP_TO_DATE
            }
        }
    }
})
