package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject

class SpringBootDevelopmentModeBlueprintTests : BehaviorSpec({

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
                frameworks {
                    springBoot.enable()
                }
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

                // And: "Agent runs in default mode"
                result.output shouldContain "DEBUG Mode: default"
            }
        }
    }

    Given("withDevTools()") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
                developmentMode.enable()
                frameworks {
                    springBoot {
                        withDevTools()
                    }
                }
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Run dev") {
            val result = project.runTask("dev")

            Then("Agent runs in rebuild mode") {
                result.output shouldContain "DEBUG Mode: rebuild"
            }
        }
    }

    Given("kotlin.enable() AND build profile set") {
        project.setUp {
            """
            general {
                buildProfiles {
                    active("test")
                }
            }
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
                developmentMode.enable()
                frameworks {
                    springBoot.enable()
                }
            }
            """.trimIndent()
        }
        project.writeAppKt { "println(\"SPRING_PROFILES_ACTIVE=\" + System.getenv()[\"SPRING_PROFILES_ACTIVE\"])" }

        When("Run 'dev'") {
            val result = project.runTask("dev")

            Then("SPRING_PROFILES_ACTIVE environment variable is set") {
                result.output shouldContain "SPRING_PROFILES_ACTIVE=test"
            }
        }
    }
})
