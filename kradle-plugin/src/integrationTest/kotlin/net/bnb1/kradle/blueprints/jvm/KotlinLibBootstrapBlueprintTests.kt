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
            }

            Then("Project files and directories are created") {
                project.projectDir.resolve(".git").shouldExist()
                project.projectDir.resolve(".gitignore").shouldExist()
                project.projectDir.resolve("gradlew").shouldExist()
                project.projectDir.resolve("src/main/kotlin/com/example").shouldExist()
                project.projectDir.resolve("src/main/resources").shouldExist()
                project.projectDir.resolve("src/main/extra").shouldExist()
                project.projectDir.resolve("src/test/kotlin/com/example").shouldExist()
                project.projectDir.resolve("src/test/resources").shouldExist()
                project.projectDir.resolve("src/benchmark/kotlin").shouldExist()
                project.projectDir.resolve("detekt-config.yml").shouldExist()
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
    }
})
