package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class DokkaBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp("app") {
            """
           jvm {
               documentation.enable()
           }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task generateDocumentation is available") {
                project.shouldHaveTask("generateDocumentation")
            }
        }

        // Raises OutOfMemoryError: Metaspace
        xWhen("Run generateDocumentation") {
            val result = project.runTask("generateDocumentation")

            Then("Succeed") {
                result.task(":generateDocumentation")!!.outcome shouldBe TaskOutcome.SUCCESS
            
                // And: "Documentation should exist"
                project.buildDir.resolve("docs/index.html").shouldExist()
            }
        }

        // Raises OutOfMemoryError: Metaspace
        xAnd("package.md in project directory") {
            project.projectDir.resolve("package.md").writeText(
                """
                # Module app
                
                Hello package.md
                """.trimIndent()
            )

            When("Run generateDocumentation") {
                project.runTask("generateDocumentation")

                Then("Documentation should contain package text") {
                    val output = project.buildDir.resolve("docs/index.html")
                    output.readText() shouldContain "Hello package.md"
                }
            }
        }

        // Raises OutOfMemoryError: Metaspace
        xAnd("package.md in source directory") {
            project.projectDir.resolve("src/main/kotlin/com/example/package.md").writeText(
                """
                # Module app
                
                Hello package.md
                """.trimIndent()
            )

            When("Run generateDocumentation") {
                project.runTask("generateDocumentation")

                Then("Documentation should contain package text") {
                    val output = project.buildDir.resolve("docs/index.html")
                    output.readText() shouldContain "Hello package.md"
                }
            }
        }

        // Raises OutOfMemoryError: Metaspace
        xAnd("module.md in project directory") {
            project.projectDir.resolve("module.md").writeText(
                """
                # Module app
                
                Hello module.md
                """.trimIndent()
            )

            When("Run generateDocumentation") {
                project.runTask("generateDocumentation")

                Then("Documentation should contain module text") {
                    val output = project.buildDir.resolve("docs/index.html")
                    output.readText() shouldContain "Hello module.md"
                }
            }
        }

        // Raises OutOfMemoryError: Metaspace
        xAnd("module.md in source directory") {
            project.projectDir.resolve("src/main/kotlin/com/example/module.md").writeText(
                """
                # Module app
                
                Hello module.md
                """.trimIndent()
            )

            When("Run generateDocumentation") {
                project.runTask("generateDocumentation")

                Then("Documentation should contain module text") {
                    val output = project.buildDir.resolve("docs/index.html")
                    output.readText() shouldContain "Hello module.md"
                }
            }
        }
    }
})
