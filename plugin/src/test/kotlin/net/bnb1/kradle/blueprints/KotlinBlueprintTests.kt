package net.bnb1.kradle.blueprints

import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec

class KotlinBlueprintTests : PluginSpec({

    test("Kotlin version property is set") {
        bootstrapAppProject()
        addTask("testTask", "println(\"Result: \${project.properties[\"kotlinVersion\"]}\")")

        val result = runTask("testTask")

        result.output shouldContain "Result: 1.4.31"
    }

    test("Check Kotlin dependencies") {
        bootstrapAppProject()

        val result = runTask("dependencies", "--configuration", "runtimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib:1.4.31"
        result.output shouldContain "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31"
        result.output shouldContain "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1"
    }

    test("Check Kotlin test dependencies") {
        bootstrapAppProject()

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-test:1.4.31"
    }
})