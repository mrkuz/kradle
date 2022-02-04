package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import net.bnb1.kradle.execute

const val MINIFIED_THRESHOLD = 4L * 1024L * 1024L

class ShadowBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp("app") {
            """
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
                packaging.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Check for tasks") {

            Then("Task uberJar is available") {
                project.shouldHaveTask("uberJar")
            }
        }

        When("Run uberJar") {
            project.runTask("uberJar")

            Then("Jar file is created") {
                val jarFile = project.buildDir.resolve("libs/app-1.0.0-uber.jar")
                jarFile.shouldExist()

                // And: "Jar file is executable"
                "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"

                // And: "Jar file size is not minified"
                jarFile.length() shouldBeGreaterThan MINIFIED_THRESHOLD
            }
        }
    }

    Given("uberJar.minimize = true") {
        project.setUp("app") {
            """
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
                packaging {
                    uberJar {
                        minimize(true)
                    }
                }
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Run uberJar") {
            project.runTask("uberJar")

            Then("Jar file is created") {
                val jarFile = project.buildDir.resolve("libs/app-1.0.0-uber.jar")
                jarFile.shouldExist()

                // And: "Jar file is executable"
                "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"

                // And: "Jar file size is minified"
                jarFile.length() shouldBeLessThan MINIFIED_THRESHOLD
            }
        }
    }
})
