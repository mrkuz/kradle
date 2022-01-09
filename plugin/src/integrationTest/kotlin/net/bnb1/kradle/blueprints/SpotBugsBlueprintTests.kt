package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome

class SpotBugsBlueprintTests : IntegrationSpec({

    Given("Default configuration") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                codeAnalysis.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        When("Run spotbugs") {
            runTask("spotbugsMain")

            Then("Report is generated") {
                buildDir.resolve("reports/spotbugs/main.html").shouldExist()
            }
        }

        When("Run check") {
            val result = runTask("check")

            Then("spotbugs should be executed") {
                result.task(":spotbugsMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Check dependencies") {
            val result = runTask("dependencies", "--configuration", "spotbugs")

            Then("spotbugs should be available") {
                result.output shouldContain "com.github.spotbugs:spotbugs"
            }
        }
    }

    Given("useFbContrib() and useFindSecBugs()") {
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

        When("Check dependencies") {
            val result = runTask("dependencies", "--configuration", "spotbugsPlugins")

            Then("findsecbugs and sb-contrib should be available") {
                result.output shouldContain "com.h3xstream.findsecbugs:findsecbugs-plugin"
                result.output shouldContain "com.mebigfatguy.sb-contrib:sb-contrib"
            }
        }
    }
})
