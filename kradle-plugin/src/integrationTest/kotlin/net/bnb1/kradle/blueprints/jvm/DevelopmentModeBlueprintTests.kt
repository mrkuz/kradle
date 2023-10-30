package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.Catalog
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
                result.output shouldContain "DEBUG Project root: ${project.projectDir.canonicalPath}"

                // And: "Agent runs in default mode"
                result.output shouldContain "DEBUG Mode: default"
            }
        }
    }

    Given("Default configuration AND dev dependencies") {
        project.writeSettingsFile()
        project.buildFile.writeText(
            """
            plugins {
                id("org.jetbrains.kotlin.jvm") version "${Catalog.Versions.kotlin}"
                id("net.bitsandbobs.kradle")
            }

            group = "com.example"
            version = "1.0.0"

            kradle {
                jvm {
                    targetJvm("${Catalog.Versions.jvm}")
                    kotlin.enable()
                    application {
                        mainClass("com.example.demo.AppKt")
                    }
                    developmentMode.enable()
                }
            }

            dependencies {
                add("kradleDev", "org.springframework.boot:spring-boot-devtools:2.6.5")
            }
            """.trimIndent()
        )
        project.writeHelloWorldAppKt()

        When("Check dependencies") {

            Then("Dev dependencies are not available in runtime classpath") {
                project.shouldNotHaveDependency("runtimeClasspath", "org.springframework.boot:spring-boot-devtools")
            }

            Then("Dev dependencies are available in kradleDev") {
                project.shouldHaveDependency("kradleDev", "org.springframework.boot:spring-boot-devtools")
            }
        }
    }

    Given("Default configuration AND build profiles") {
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
            }
            """.trimIndent()
        }
        project.writeAppKt { "println(\"KRADLE_PROFILE=\" + System.getenv()[\"KRADLE_PROFILE\"])" }

        When("Run dev") {
            val result = project.runTask("dev")

            Then("KRADLE_PROFILE environment variable is set") {
                result.output shouldContain "KRADLE_PROFILE=test"
            }
        }
    }
})
