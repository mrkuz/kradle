package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

class AllOpenBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
            }
            """.trimIndent()
        }

        When("Check for plugins") {

            Then("All-open plugin is applied") {
                project.shouldHavePlugin(AllOpenGradleSubplugin::class)
            }
        }
    }
})
