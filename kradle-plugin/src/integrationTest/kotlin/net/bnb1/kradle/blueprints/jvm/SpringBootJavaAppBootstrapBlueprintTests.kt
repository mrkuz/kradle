package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class SpringBootJavaAppBootstrapBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                frameworks {
                    springBoot.enable()
                }
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task bootstrapJavaApp is available") {
                project.shouldHaveTask("bootstrapJavaApp")
            }
        }

        When("Run bootstrapJavaApp") {
            val result = project.runTask("bootstrapJavaApp")

            Then("Succeed") {
                result.task(":bootstrapJavaApp")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Project files and directories are created"
                project.projectDir.resolve("gradlew").shouldExist()
                project.projectDir.resolve("src/main/resources").shouldExist()
                project.projectDir.resolve("src/main/resources/application.properties").shouldExist()
                project.projectDir.resolve("src/main/extra").shouldExist()
                project.projectDir.resolve("src/test/java/com/example/demo").shouldExist()
                project.projectDir.resolve("src/test/resources").shouldExist()
                project.projectDir.resolve("src/benchmark/java").shouldExist()
                project.projectDir.resolve("README.md").shouldExist()
                project.projectDir.resolve("LICENSE").shouldExist()
                project.projectDir.resolve("project.properties").shouldExist()

                val appKt = project.projectDir.resolve("src/main/java/com/example/demo/App.java")
                appKt.shouldExist()

                val lines = appKt.readLines()
                lines.forOne { it shouldBe "package com.example.demo;" }
                lines.forOne { it shouldBe "import org.springframework.boot.autoconfigure.SpringBootApplication;" }
            }
        }

        When("Run bootstrap") {
            val result = project.runTask("bootstrap")

            Then("bootstrapJavaApp is called") {
                result.task(":bootstrapJavaApp")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})
