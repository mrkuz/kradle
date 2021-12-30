package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class PmdBlueprintTests : PluginSpec({

    test("Run PMD") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        runTask("pmdMain")

        buildDir.resolve("reports/pmd/main.html").shouldExist()
    }

    test("Run PMD with 'check'") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        val result = runTask("check")

        result.task(":pmdMain")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Check PMD dependencies") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "kradlePmd")

        result.output shouldContain "net.sourceforge.pmd:pmd-java"
    }
})
