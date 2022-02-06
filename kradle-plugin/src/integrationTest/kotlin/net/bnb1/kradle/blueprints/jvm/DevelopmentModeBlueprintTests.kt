package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject

class DevelopmentModeBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
           jvm {
               kotlin.enable()
               application {
                   mainClass("com.example.demo.AppKt")
               }
               developmentMode.enable()
           }
            """.trimIndent()
        }
        project.writeAppKt { "println(\"KRADLE_DEV_MODE=\" + System.getenv()[\"KRADLE_DEV_MODE\"])" }

        When("Check for tasks") {

            Then("Task dev is available") {
                project.shouldHaveTask("dev")
            }
        }

        When("Run dev") {
            val result = project.runTask("dev")

            Then("KRADLE_DEV_MODE environment variable is set") {
                result.output shouldContain "KRADLE_DEV_MODE=true"

                // And: "Agent is attached"
                result.output shouldContain "DEBUG Project root: ${project.projectDir.absolutePath}"
            }
        }
    }
})
