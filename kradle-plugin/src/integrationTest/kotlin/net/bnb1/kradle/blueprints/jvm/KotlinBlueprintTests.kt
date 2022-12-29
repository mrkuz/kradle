package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import net.bnb1.kradle.TestProject
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin

class KotlinBlueprintTests : BehaviorSpec({

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

            Then("Serialization plugin is applied") {
                project.shouldHavePlugin(SerializationGradleSubplugin::class)
            }
        }

        When("Check for dependencies") {

            Then("kotlin-stdlib is available") {
                project.shouldHaveDependency("implementation", "org.jetbrains.kotlin:kotlin-stdlib")

                // And: "kotlin-reflect is available"
                project.shouldHaveDependency("implementation", "org.jetbrains.kotlin:kotlin-reflect")

                // And: "kotlin-test is available"
                project.shouldHaveDependency("testImplementation", "org.jetbrains.kotlin:kotlin-test")
            }
        }

        When("Check for properties") {

            Then("kotlinVersion is set") {
                project.shouldHaveProperty("kotlinVersion", Regex("[0-9]+\\.[0-9]+\\.[0-9]+"))
            }
        }
    }

    Given("kotlin.useCoroutines()") {
        project.setUp {
            """
            jvm {
                kotlin {
                    useCoroutines()
                }
            }
            """.trimIndent()
        }

        When("Check for dependencies") {

            Then("kotlinx-coroutines-core is available") {
                project.shouldHaveDependency("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core")
            }
        }
    }
})
