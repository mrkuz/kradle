package net.bnb1.kradle.plugins

import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.GradleRunner

class KradleCompatAppPluginTests : PluginSpec({

    test("Check available tasks") {
        bootstrapCompatAppProject()

        val result = runTask("tasks")

        result.output shouldContain "analyzeCode "
        result.output shouldContain "analyzeDependencies "
        result.output shouldContain "buildImage "
        result.output shouldContain "generateBuildProperties "
        result.output shouldContain "generateDocumentation "
        result.output shouldContain "lint "
        result.output shouldContain "package "
        result.output shouldContain "runBenchmarks "
        result.output shouldContain "showDependencyUpdates "
        result.output shouldContain "uberJar "
        result.output shouldNotContain "install "
    }

    test("Check default Kotlin version") {
        bootstrapCompatAppProject()

        val result = runTask("dependencies", "--configuration", "runtimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib:1.6.0"
        result.output shouldNotContain "org.jetbrains.kotlin:kotlin-stdlib:1.5.31"
    }

    // Requires latest version of kradle installed to your local repository
    xtest("Override Kotlin version") {
        settingsFile.writeText(
            """
            pluginManagement {
                repositories {
                    gradlePluginPortal()
                    mavenLocal()
                }
            }
            
            rootProject.name = "app"
            """.trimIndent()
        )
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.6.10"
               id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.test"
            version = "1.0.0"
            
            kradle {
                mainClass("com.test.App")
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("dependencies", "--configuration", "runtimeClasspath")
            .build()

        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib:1.6.10"
    }
})