package net.bnb1.kradle.presets

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class KotlinJvmApplicationPresetTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Project using Kotlin application preset") {
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
                kotlinJvmApplication {
                    jvm {
                        targetJvm("${Catalog.Versions.jvm}")
                        application {
                            mainClass("com.example.demo.AppKt")
                        }
                    }
                }
            }
            """.trimIndent()
        )
        project.writeHelloWorldAppKt()

        When("Run 'run'") {
            val result = project.runTask("run")

            Then("Succeed") {
                result.task(":run")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
