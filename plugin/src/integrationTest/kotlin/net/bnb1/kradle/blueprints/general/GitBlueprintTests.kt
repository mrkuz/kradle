package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import net.bnb1.kradle.support.plugins.GitPlugin
import org.gradle.testkit.runner.TaskOutcome

class GitBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                git.enable()
            }
            """.trimIndent()
        }

        When("Check for plugins") {

            Then("Git plugin is applied") {
                project.shouldHavePlugin(GitPlugin::class)
            }
        }

        When("Check for tasks") {

            Then("Task generateGitignore is available") {
                project.shouldHaveTask("generateGitignore")
            }
        }

        When("Check for project properties") {

            Then("gitCommit is not set") {
                project.shouldNotHaveProperty("gitCommit")
            }
        }

        When("Run generateGitignore") {
            val result = project.runTask("generateGitignore")

            Then("Succeed") {
                result.task(":generateGitignore")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then(".gitignore exists") {
                project.projectDir.resolve(".gitignore").shouldExist()
            }
        }

        And(".gitignore exists") {
            val gitignore = project.projectDir.resolve(".gitignore")
            gitignore.writeText("***")

            When("Run generateGitignore") {
                project.runTask("generateGitignore")

                Then("Do nothing") {
                    val lines = gitignore.readLines()
                    lines.shouldHaveSize(1)
                    lines.forOne { it shouldBe "***" }
                }
            }
        }

        And("Project is a Git project") {
            project.gitInit()

            When("Check for project properties") {

                Then("gitCommit is set") {
                    project.shouldHaveProperty("gitCommit", Regex("[a-z0-9]{7}"))
                }
            }
        }
    }
})
