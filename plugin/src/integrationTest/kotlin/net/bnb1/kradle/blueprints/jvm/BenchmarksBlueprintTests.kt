package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.benchmark.gradle.BenchmarksPlugin
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

class BenchmarksBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                benchmark.enable()
            }
            """.trimIndent()
        }

        val sourceDir = project.projectDir.resolve("src/benchmark/kotlin/com/example/demo")
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

        When("Check for plugins") {

            Then("Benchmarks plugin is applied") {
                project.shouldHavePlugin(BenchmarksPlugin::class)
            }

            Then("All-open plugin is applied") {
                project.shouldHavePlugin(AllOpenGradleSubplugin::class)
            }
        }

        When("Check for tasks") {

            Then("Task runBenchmarks is available") {
                project.shouldHaveTask("runBenchmarks")
            }
        }

        When("Check dependencies") {

            Then("JMH is available") {
                project.shouldHaveDependency("benchmarkImplementation", "org.openjdk.jmh:jmh-core")
            }
        }

        When("Run runBenchmarks") {
            val result = project.runTask("runBenchmarks")

            Then("Succeed") {
                result.task(":runBenchmarks")!!.outcome shouldBe TaskOutcome.SUCCESS
            }

            Then("Benchmarks should be run") {
                result.output shouldContain "Running 'main' benchmarks for 'benchmark'"
                result.output shouldContain "com.example.DummyBenchmark.doNothing"
                result.output shouldContain "Iteration 1:"
                result.output shouldContain Regex("Success: [0-9]+")
            }

            Then("Report is available") {
                project.buildDir.resolve("reports/benchmarks").shouldExist()
            }
        }
    }

    Given("benchmark.jmhVersion = 1.20") {
        project.setUp {
            """
            jvm {
                kotlin.enable()
                benchmark {
                    jmhVersion("1.20")
                }
            }
            """.trimIndent()
        }

        When("Check dependencies") {

            Then("Specified JMH version is used") {
                project.shouldHaveDependency("benchmarkImplementation", "org.openjdk.jmh:jmh-core:1.20")
            }
        }
    }
})
