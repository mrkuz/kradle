package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class CodeCoverageBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                codeCoverage.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task analyzeTestCoverage is available") {
                project.shouldHaveTask("analyzeTestCoverage")
            }
        }

        When("Run check") {
            val result = project.runTask("check")

            Then("analyzeTestCoverage is called") {
                result.task(":analyzeTestCoverage")!!.outcome shouldBe TaskOutcome.UP_TO_DATE
            }
        }
    }
})
