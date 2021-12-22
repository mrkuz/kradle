package net.bnb1.kradle.features

import io.kotest.inspectors.forOne
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import net.bnb1.kradle.PluginSpec

class KotlinBlueprintTests : PluginSpec({

    test("Kotlin version property is set") {
        bootstrapCompatAppProject()
        addTask("testTask", "println(\"Result: \${project.properties[\"kotlinVersion\"]}\")")

        val result = runTask("testTask")

        result.output.lines().forOne { it shouldMatch Regex("Result: [0-9]+\\.[0-9]+\\.[0-9]+") }
    }

    test("Check Kotlin dependencies") {
        bootstrapCompatAppProject()

        val result = runTask("dependencies", "--configuration", "runtimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib"
        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
        result.output shouldContain "org.jetbrains.kotlin:kotlin-reflect"
        result.output shouldContain "org.jetbrains.kotlinx:kotlinx-coroutines-core"
    }

    test("Check Kotlin test dependencies") {
        bootstrapCompatAppProject()

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-test"
    }
})
