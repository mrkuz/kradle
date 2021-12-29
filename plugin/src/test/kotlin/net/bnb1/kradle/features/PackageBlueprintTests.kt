package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import net.bnb1.kradle.execute

class PackageBlueprintTests : PluginSpec({

    test("Check 'package' alias") {
        bootstrapProject {
            """
            jvm {
                packaging.enable()
            }
            """.trimIndent()
        }

        val result = runTask("tasks")

        result.output shouldContain "package "
    }

    test("Create and run JAR") {
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

        runTask("jar")

        val jarFile = buildDir.resolve("libs/app-1.0.0.jar")
        jarFile.shouldExist()
        "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"
    }
})
