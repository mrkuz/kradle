package net.bnb1.kradle.blueprints

import io.kotest.inspectors.forOne
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.IntegrationSpec

class JavaBootstrapBlueprintTests : IntegrationSpec({

    Given("App project") {
        bootstrapProject {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
            }
            """.trimIndent()
        }

        When("Run bootstrap") {
            runTask("bootstrap")

            Then("Project files and directories should be generated") {
                projectDir.resolve(".git").shouldExist()
                projectDir.resolve(".gitignore").shouldExist()
                projectDir.resolve("gradlew").shouldExist()
                projectDir.resolve("src/main/resources").shouldExist()
                projectDir.resolve("src/main/extra").shouldExist()
                projectDir.resolve("src/test/java/com/example/demo").shouldExist()
                projectDir.resolve("src/test/resources").shouldExist()
                projectDir.resolve("src/benchmark/java").shouldExist()
                projectDir.resolve("checkstyle.xml").shouldExist()
                projectDir.resolve("README.md").shouldExist()
                projectDir.resolve("LICENSE").shouldExist()
                projectDir.resolve("project.properties").shouldExist()

                val appKt = projectDir.resolve("src/main/java/com/example/demo/App.java")
                appKt.shouldExist()

                val lines = appKt.readLines()
                lines.forOne { it shouldBe "package com.example.demo;" }
            }
        }
    }

    Given("Lib project") {
        bootstrapProject {
            """
            general {
                bootstrap.enable()
            }
            jvm {
                java.enable()
                library.enable()
            }
            """.trimIndent()
        }

        When("Run bootstrap") {
            runTask("bootstrap")

            Then("Project files and directories should be generated") {
                projectDir.resolve(".git").shouldExist()
                projectDir.resolve(".gitignore").shouldExist()
                projectDir.resolve("gradlew").shouldExist()
                projectDir.resolve("src/main/java/com/example").shouldExist()
                projectDir.resolve("src/main/resources").shouldExist()
                projectDir.resolve("src/main/extra").shouldExist()
                projectDir.resolve("src/test/java/com/example").shouldExist()
                projectDir.resolve("src/test/resources").shouldExist()
                projectDir.resolve("src/benchmark/java").shouldExist()
                projectDir.resolve("checkstyle.xml").shouldExist()
                projectDir.resolve("README.md").shouldExist()
                projectDir.resolve("LICENSE").shouldExist()
                projectDir.resolve("project.properties").shouldExist()
            }
        }
    }
})
