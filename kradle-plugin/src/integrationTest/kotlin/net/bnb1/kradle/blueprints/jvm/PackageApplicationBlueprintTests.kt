package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import net.bnb1.kradle.execute

class PackageApplicationBlueprintTests : BehaviorSpec({

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

        When("Run jar") {
            project.runTask("jar")

            Then("Jar file is created") {
                val jarFile = project.buildDir.resolve("libs/app-1.0.0.jar")
                jarFile.shouldExist()

                // And: "Jar file is executable"
                "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"
            }
        }
    }
})
