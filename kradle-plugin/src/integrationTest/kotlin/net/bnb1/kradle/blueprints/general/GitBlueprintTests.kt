package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import net.bnb1.kradle.support.plugins.GitPlugin
import org.eclipse.jgit.api.Git
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

                // And: ".gitignore exists"
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

                    // And: "gitBranch is set"
                    project.shouldHaveProperty("gitBranch", "main")

                    // And: "gitBranchPrefix is set"
                    project.shouldHaveProperty("gitBranchPrefix", "main")
                }
            }
        }

        And("Feature branch is checked out") {
            project.gitInit()
            Git.open(project.projectDir)
                .checkout()
                .setName("feature/whatever")
                .setCreateBranch(true).call()

            When("Check for project properties") {

                Then("gitBranch is set") {
                    project.shouldHaveProperty("gitBranch", "feature/whatever")

                    // And: "gitBranchPrefix is set"
                    project.shouldHaveProperty("gitBranchPrefix", "feature")
                }
            }
        }
    }
})
