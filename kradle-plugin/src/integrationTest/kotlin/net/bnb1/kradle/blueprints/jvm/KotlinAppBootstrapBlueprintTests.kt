package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forOne
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotStartWith
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class KotlinAppBootstrapBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
            }
            """.trimIndent()
        }

        When("Check for tasks") {

            Then("Task bootstrapKotlinApp is available") {
                project.shouldHaveTask("bootstrapKotlinApp")
            }
        }

        When("Run bootstrapKotlinApp") {
            val result = project.runTask("bootstrapKotlinApp")

            Then("Succeed") {
                result.task(":bootstrapKotlinApp")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Project files and directories are created"
                project.projectDir.resolve("gradlew").shouldExist()
                project.projectDir.resolve("src/main/resources").shouldExist()
                project.projectDir.resolve("src/main/extra").shouldExist()
                project.projectDir.resolve("src/test/kotlin/com/example/demo").shouldExist()
                project.projectDir.resolve("src/test/resources").shouldExist()
                project.projectDir.resolve("src/benchmark/kotlin").shouldExist()
                project.projectDir.resolve("README.md").shouldExist()
                project.projectDir.resolve("LICENSE").shouldExist()
                project.projectDir.resolve("project.properties").shouldExist()

                val appKt = project.projectDir.resolve("src/main/kotlin/com/example/demo/App.kt")
                appKt.shouldExist()

                val lines = appKt.readLines()
                lines.forOne { it shouldBe "package com.example.demo" }
                lines.forAll { it shouldNotStartWith "import " }
            }
        }

        When("Run bootstrap") {
            val result = project.runTask("bootstrap")

            Then("bootstrapKotlinApp is called") {
                result.task(":bootstrapKotlinApp")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run bootstrap and build") {
            project.runTask("bootstrap")
            val result = project.runTask("build")

            Then("build is successful") {
                result.task(":build")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Run bootstrap and run") {
            project.runTask("bootstrap")
            val result = project.runTask("run")

            Then("run is successful") {
                result.task(":run")!!.outcome shouldBe TaskOutcome.SUCCESS
                result.output shouldContain "Hello World!"
            }
        }
    }
})
