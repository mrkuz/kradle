package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class JavaLibBootstrapBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                java.enable()
                library.enable()
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task bootstrapJavaLib is available") {
                project.shouldHaveTask("bootstrapJavaLib")
            }
        }

        When("Run bootstrapJavaLib") {
            val result = project.runTask("bootstrapJavaLib")

            Then("Succeed") {
                result.task(":bootstrapJavaLib")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Project files and directories are created"
                project.projectDir.resolve(".git").shouldExist()
                project.projectDir.resolve(".gitignore").shouldExist()
                project.projectDir.resolve("gradlew").shouldExist()
                project.projectDir.resolve("src/main/java/com/example").shouldExist()
                project.projectDir.resolve("src/main/resources").shouldExist()
                project.projectDir.resolve("src/main/extra").shouldExist()
                project.projectDir.resolve("src/test/java/com/example").shouldExist()
                project.projectDir.resolve("src/test/resources").shouldExist()
                project.projectDir.resolve("src/benchmark/java").shouldExist()
                project.projectDir.resolve("checkstyle.xml").shouldExist()
                project.projectDir.resolve("README.md").shouldExist()
                project.projectDir.resolve("LICENSE").shouldExist()
                project.projectDir.resolve("project.properties").shouldExist()
            }
        }

        When("Run bootstrap") {
            val result = project.runTask("bootstrap")

            Then("bootstrapJavaLib is called") {
                result.task(":bootstrapJavaLib")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
