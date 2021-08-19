package net.bnb1.kradle.plugins

import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.bnb1.kradle.PluginSpec

class KradleLibPluginTests : PluginSpec({

    test("Check available tasks") {
        bootstrapLibProject()

        val result = runTask("tasks")

        result.output shouldContain "analyzeCode "
        result.output shouldContain "analyzeDependencies "
        result.output shouldContain "generateBuildProperties "
        result.output shouldContain "generateDocumentation "
        result.output shouldContain "install "
        result.output shouldContain "lint "
        result.output shouldContain "package "
        result.output shouldContain "runBenchmarks "
        result.output shouldContain "showDependencyUpdates "
        result.output shouldNotContain "buildImage "
        result.output shouldNotContain "uberJar "
    }
})