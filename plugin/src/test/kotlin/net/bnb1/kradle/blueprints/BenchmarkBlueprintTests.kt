package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class BenchmarkBlueprintTests : PluginSpec({

    test("Run benchmarks") {
        bootstrapAppProject()

        val sourceDir = projectDir.resolve("src/benchmark/kotlin/com/example")
        sourceDir.mkdirs()
        sourceDir.resolve("DummyBenchmark.kt").writeText(
            """
            package com.example

            import org.openjdk.jmh.annotations.*

            @State(Scope.Benchmark)
            @Fork(1)
            @Warmup(iterations = 0)
            @Measurement(iterations = 1)
            class DummyBenchmark {

                @Benchmark
                fun doNothing() {}
            }
            """.trimIndent()
        )

        val result = runTask("benchmark")

        result.output shouldContain "Running 'main' benchmarks for 'benchmark'"
        result.output shouldContain "com.example.DummyBenchmark.doNothing"
        result.output shouldContain "Iteration 1:"
        result.output shouldContain Regex("Success: [0-9]+")

        buildDir.resolve("reports/benchmarks").shouldExist()
    }
})