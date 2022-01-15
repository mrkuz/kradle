package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import net.bnb1.kradle.TestProject
import net.bnb1.kradle.support.plugins.BuildPropertiesPlugin
import org.gradle.testkit.runner.TaskOutcome

private const val LINES_WITHOUT_GIT = 4
private const val LINES_WITH_GIT = 5

class BuildPropertiesBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                buildProperties.enable()
            }
            jvm {
                kotlin.enable()
            }
            """.trimIndent()
        }

        When("Run generateBuildProperties") {
            val result = project.runTask("generateBuildProperties")

            Then("Succeed") {
                result.task(":generateBuildProperties")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("build.properties exists") {
                val output = project.buildDir.resolve("resources/main/build.properties")
                output.shouldExist()

                val lines = output.readLines()
                lines.shouldHaveSize(LINES_WITHOUT_GIT)
                lines.forOne { it shouldBe "project.name=test" }
                lines.forOne { it shouldBe "project.group=com.example" }
                lines.forOne { it shouldBe "project.version=1.0.0" }
                lines.forOne { it shouldMatch Regex("build.timestamp=[0-9]{10}") }
            }
        }

        When("Run processResources") {
            val result = project.runTask("processResources")

            Then("generateBuildProperties is called") {
                result.task(":generateBuildProperties")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Check for tasks") {

            Then("Task generateBuildProperties is available") {
                project.shouldHaveTask("generateBuildProperties")
            }
        }

        When("Check for plugins") {

            Then("Build properties plugin is applied") {
                project.shouldHavePlugin(BuildPropertiesPlugin::class)
            }
        }
    }

    Given("Git project") {
        project.setUp {
            """
            general {
                buildProperties.enable()
                git.enable()
            }
            jvm {
                kotlin.enable()
            }
            """.trimIndent()
        }
        project.gitInit()

        When("Run generateBuildProperties") {
            val result = project.runTask("generateBuildProperties")

            Then("build.properties contains Git commit id") {
                val output = project.buildDir.resolve("resources/main/build.properties")
                output.shouldExist()

                val lines = output.readLines()
                lines.shouldHaveSize(LINES_WITH_GIT)
                lines.forOne { it shouldMatch Regex("git.commit-id=[a-z0-9]{7}") }
            }
        }
    }
})
