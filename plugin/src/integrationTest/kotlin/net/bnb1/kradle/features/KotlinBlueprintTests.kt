package net.bnb1.kradle.features

import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.bnb1.kradle.IntegrationSpec

class KotlinBlueprintTests : IntegrationSpec({

    Given("Default configuration") {
        bootstrapProject {
            """
            jvm {
                kotlin.enable()
            }
            """.trimIndent()
        }

        When("Check dependencies") {
            val result = runTask("dependencies", "--configuration", "runtimeClasspath")

            Then("Kotlin dependencies should be present") {
                result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib"
                result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
                result.output shouldContain "org.jetbrains.kotlin:kotlin-reflect"
                result.output shouldNotContain "org.jetbrains.kotlinx:kotlinx-coroutines-core"
            }
        }
    }

    Given("useCoroutines()") {
        bootstrapProject {
            """
            jvm {
                kotlin {
                    useCoroutines()
                }
            }
            """.trimIndent()
        }

        When("Check dependencies") {
            val result = runTask("dependencies", "--configuration", "runtimeClasspath")

            Then("Kotlin coroutines dependency should be present") {
                result.output shouldContain "org.jetbrains.kotlinx:kotlinx-coroutines-core"
            }
        }
    }
})
