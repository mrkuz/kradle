package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class CheckstyleBlueprintTests : PluginSpec({

    test("Run checkstyle") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                lint.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        runTask("checkstyleMain")

        buildDir.resolve("reports/checkstyle/main.html").shouldExist()
    }

    test("Generate checkstyle.xml") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                lint.enable()
            }
            """.trimIndent()
        }

        runTask("generateCheckstyleConfig")

        projectDir.resolve("checkstyle.xml").shouldExist()
    }

    test("Run checkstyle with 'check'") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                lint.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        val result = runTask("check")

        result.task(":checkstyleMain")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Check checkstyle dependencies") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                lint.enable()
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "kradleCheckstyle")

        result.output shouldContain "om.puppycrawl.tools:checkstyle"
    }
})
