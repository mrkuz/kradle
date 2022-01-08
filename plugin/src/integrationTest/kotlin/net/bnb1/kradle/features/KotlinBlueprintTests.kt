package net.bnb1.kradle.features

import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.bnb1.kradle.IntegrationSpec

class KotlinBlueprintTests : IntegrationSpec({

    test("Check Kotlin dependencies") {
        bootstrapProject {
            """
            jvm {
                kotlin.enable()
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "runtimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib"
        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
        result.output shouldContain "org.jetbrains.kotlin:kotlin-reflect"
        result.output shouldNotContain "org.jetbrains.kotlinx:kotlinx-coroutines-core"
    }

    test("Check Kotlin coroutines dependencies") {
        bootstrapProject {
            """
            jvm {
                kotlin {
                    useCoroutines()
                }
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "runtimeClasspath")

        result.output shouldContain "org.jetbrains.kotlinx:kotlinx-coroutines-core"
    }
})
