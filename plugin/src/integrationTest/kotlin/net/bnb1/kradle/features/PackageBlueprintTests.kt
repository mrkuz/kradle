package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import net.bnb1.kradle.execute

class PackageBlueprintTests : IntegrationSpec({

    Given("Default configuration") {
        bootstrapProject("app") {
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
        writeAppKt("println(\"Hello World\")")

        When("List tasks") {
            val result = runTask("tasks")

            Then("package is available") {
                result.output shouldContain "package "
            }
        }

        When("Run jar") {
            runTask("jar")

            Then("Jar file is created") {
                val jarFile = buildDir.resolve("libs/app-1.0.0.jar")
                jarFile.shouldExist()

                And("Executable") {
                    "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"
                }
            }
        }
    }
})
