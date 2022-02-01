package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin

class OwaspDependencyCheckBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                vulnerabilityScan.enable()
            }
            """.trimIndent()
        }

        When("Check for plugins") {

            Then("OWASP dependency check plugin is applied") {
                project.shouldHavePlugin(DependencyCheckPlugin::class)
            }
        }

        When("Check for tasks") {

            Then("Task analyzeDependencies is available") {
                project.shouldHaveTask("analyzeDependencies")
            }
        }

        // Takes very long
        xWhen("Run analyzeDependencies") {
            val result = project.runTask("analyzeDependencies")

            Then("Succeed") {
                result.task(":analyzeDependencies")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Report is created") {
                project.buildDir.resolve("reports/dependency-check-report.html").shouldExist()
            }
        }
    }
})
