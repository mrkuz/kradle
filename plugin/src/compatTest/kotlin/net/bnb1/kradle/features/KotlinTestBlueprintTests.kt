package net.bnb1.kradle.features

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec

class KotlinTestBlueprintTests : CompatSpec({

    test("Check kotest dependencies") {
        bootstrapProject {
            """
            jvm {
                kotlin {
                    test {
                        useKotest()
                    }
                }
                test {
                    withJunitJupiter()
                }
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "io.kotest:kotest-runner-junit5"
        result.output shouldContain "io.kotest:kotest-assertions-core"
    }

    test("Check mockk dependencies") {
        bootstrapProject {
            """
            jvm {
                kotlin {
                    test {
                        useMockk()
                    }
                }
                test.enable()
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "io.mockk:mockk"
    }
})
