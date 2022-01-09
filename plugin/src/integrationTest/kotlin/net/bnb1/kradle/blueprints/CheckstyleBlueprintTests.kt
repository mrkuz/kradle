package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.IntegrationSpec
import org.gradle.testkit.runner.TaskOutcome

class CheckstyleBlueprintTests : IntegrationSpec({

    Given("Default configuration") {
        bootstrapProject {
            """
            jvm {
                java.enable()
                lint.enable()
            }
            """.trimIndent()
        }
        writeAppJava("System.out.println(\"Hello World\");")

        When("Run checkstyle") {
            runTask("checkstyleMain")

            Then("Report is generated") {
                buildDir.resolve("reports/checkstyle/main.html").shouldExist()
            }
        }

        When("Run generateCheckstyleConfig") {
            runTask("generateCheckstyleConfig")

            Then("checkstyle.xml is generated") {
                projectDir.resolve("checkstyle.xml").shouldExist()
            }
        }

        When("Run check'") {
            val result = runTask("check")

            Then("'checkstyleMain' should also run") {
                result.task(":checkstyleMain")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }

        When("Check dependencies") {
            val result = runTask("dependencies", "--configuration", "kradleCheckstyle")

            Then("checkstyle should be presetn") {
                result.output shouldContain "om.puppycrawl.tools:checkstyle"
            }
        }
    }
})
