package net.bnb1.kradle.plugins

import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import net.bnb1.kradle.PluginSpec
import org.gradle.testkit.runner.GradleRunner

class KradleAppPluginTests : PluginSpec({

    test("Check available tasks") {
        bootstrapAppProject()

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
        bootstrapAppProject()

        val result = runTask("dependencies", "--configuration", "runtimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib:1.4.31"
        result.output shouldNotContain "org.jetbrains.kotlin:kotlin-stdlib:1.5.21"
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
               id("org.jetbrains.kotlin.jvm") version "1.5.21"
               id("net.bnb1.kradle-app") version "1.0.0-SNAPSHOT"
            }
            
            group = "com.test"
            version = "1.0.0"
            
            application {
                mainClass.set("com.test.AppKt")
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("dependencies", "--configuration", "runtimeClasspath")
            .build()

        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib:1.5.21"
    }
})