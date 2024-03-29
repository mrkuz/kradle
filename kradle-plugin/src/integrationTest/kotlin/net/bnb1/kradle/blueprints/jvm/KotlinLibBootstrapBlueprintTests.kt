package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class KotlinLibBootstrapBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                kotlin.enable()
                lint.enable()
                codeAnalysis.enable()
                library.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task bootstrapKotlinLib is available") {
                project.shouldHaveTask("bootstrapKotlinLib")
            }
        }

        When("Run bootstrapKotlinLib") {
            val result = project.runTask("bootstrapKotlinLib")

            Then("Succeed") {
                result.task(":bootstrapKotlinLib")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Project files and directories are created"
                project.projectDir.resolve("gradlew").shouldExist()
                project.projectDir.resolve("src/main/kotlin/com/example").shouldExist()
                project.projectDir.resolve("src/main/resources").shouldExist()
                project.projectDir.resolve("src/main/extra").shouldExist()
                project.projectDir.resolve("src/test/kotlin/com/example").shouldExist()
                project.projectDir.resolve("src/test/resources").shouldExist()
                project.projectDir.resolve("src/benchmark/kotlin").shouldExist()
                project.projectDir.resolve("README.md").shouldExist()
                project.projectDir.resolve("LICENSE").shouldExist()
                project.projectDir.resolve("project.properties").shouldExist()
            }
        }

        When("Run bootstrap") {
            val result = project.runTask("bootstrap")

            Then("bootstrapKotlinLib is called") {
                result.task(":bootstrapKotlinLib")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run bootstrap and build") {
            project.runTask("bootstrap")
            val result = project.runTask("build")

            Then("build is successful") {
                result.task(":build")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
