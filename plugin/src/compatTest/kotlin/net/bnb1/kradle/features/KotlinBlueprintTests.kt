package net.bnb1.kradle.features

import io.kotest.inspectors.forOne
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import net.bnb1.kradle.CompatSpec

class KotlinBlueprintTests : CompatSpec({

    test("Kotlin version property is set") {
        bootstrapCompatAppProject()
        addTask("testTask", "println(\"Result: \${project.properties[\"kotlinVersion\"]}\")")

        val result = runTask("testTask")

        result.output.lines().forOne { it shouldMatch Regex("Result: [0-9]+\\.[0-9]+\\.[0-9]+") }
    }

    test("Check Kotlin test dependencies") {
        bootstrapCompatAppProject()

        val result = runTask("dependencies", "--configuration", "testRuntimeClasspath")

        result.output shouldContain "org.jetbrains.kotlin:kotlin-test"
    }
})
