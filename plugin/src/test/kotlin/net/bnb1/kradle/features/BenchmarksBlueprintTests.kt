package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class BenchmarksBlueprintTests : PluginSpec({

    test("Run benchmarks") {
        bootstrapCompatAppProject()

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

    test("Check JMH dependencies") {
        writeSettingsGradle("lib")
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-lib") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                jmhVersion("1.20")
            }
            """.trimIndent()
        )

        val result = runTask("dependencies", "--configuration", "benchmarkImplementation")

        result.output shouldContain "org.openjdk.jmh:jmh-core"
    }
})