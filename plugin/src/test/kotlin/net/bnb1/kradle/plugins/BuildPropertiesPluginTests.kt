package net.bnb1.kradle.plugins

import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.TaskOutcome

class BuildPropertiesPluginTests : PluginSpec({

    test("Generate build.properties") {
        bootstrapAppProject()

        val result = runTask("generateBuildProperties")

        result.task(":generateBuildProperties")!!.outcome shouldBe TaskOutcome.SUCCESS

        val output = buildDir.resolve("resources/main/build.properties")
        output.shouldExist()

        val lines = output.readLines()
        lines.shouldHaveSize(4)
        lines.forOne { it shouldBe "project.name=app" }
        lines.forOne { it shouldBe "project.group=com.example" }
        lines.forOne { it shouldBe "project.version=1.0.0" }
        lines.forOne { it shouldMatch Regex("build.timestamp=[0-9]{10}") }
    }

    test("Generate build.properties with Git commit id") {
        bootstrapAppProject()
        gitInit()

        val result = runTask("generateBuildProperties")

        result.task(":generateBuildProperties")!!.outcome shouldBe TaskOutcome.SUCCESS

        val output = buildDir.resolve("resources/main/build.properties")
        output.shouldExist()

        val lines = output.readLines()
        lines.shouldHaveSize(5)
        lines.forOne { it shouldBe "project.name=app" }
        lines.forOne { it shouldBe "project.group=com.example" }
        lines.forOne { it shouldBe "project.version=1.0.0" }
        lines.forOne { it shouldMatch Regex("build.timestamp=[0-9]{10}") }
        lines.forOne { it shouldMatch Regex("git.commit-id=[a-z0-9]{7}") }
    }
})