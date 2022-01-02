package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class SpotBugsBlueprintTests : PluginSpec({

    test("Run SpotBugs") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        runTask("spotbugsMain")

        buildDir.resolve("reports/spotbugs/main.html").shouldExist()
    }

    test("Run SpotBugs with 'check'") {
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

        result.task(":spotbugsMain")!!.outcome shouldBe TaskOutcome.SUCCESS
    }

    test("Check SpotBugs dependencies") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "spotbugs")

        result.output shouldContain "com.github.spotbugs:spotbugs"
    }

    test("Check SpotBugs plugin dependencies") {
        bootstrapProject {
            """
            jvm {
                java {
                    codeAnalysis {
                        spotBugs {
                            useFbContrib()
                            useFindSecBugs()
                        }
                    }
                }
                codeAnalysis.enable()
            }
            """.trimIndent()
        }

        val result = runTask("dependencies", "--configuration", "spotbugsPlugins")

        result.output shouldContain "com.h3xstream.findsecbugs:findsecbugs-plugin"
        result.output shouldContain "com.mebigfatguy.fb-contrib:fb-contrib"
    }
})
