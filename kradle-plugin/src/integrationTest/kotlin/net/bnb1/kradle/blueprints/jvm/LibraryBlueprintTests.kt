package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject
import org.gradle.api.plugins.JavaLibraryPlugin

class LibraryBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                library.enable()
            }
            """.trimIndent()
        }

        When("Check for plugins") {

            Then("Java library plugin is applied") {
                project.shouldHavePlugin(JavaLibraryPlugin::class)
            }
        }
    }
})
