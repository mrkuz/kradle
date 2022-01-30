package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class KotlinQuickStartTests : FunSpec({

    val container = TestContainer(this).container

    test("(Very) Quick Start") {
        container.execInContainer(
            "curl",
            "-O",
            "https://raw.githubusercontent.com/mrkuz/kradle/main/examples/app/settings.gradle.kts"
        )
        container.execInContainer(
            "curl",
            "-O",
            "https://raw.githubusercontent.com/mrkuz/kradle/main/examples/app/build.gradle.kts"
        )

        val bootstrapResult = container.execInContainer("gradle", "bootstrap")
        bootstrapResult.exitCode shouldBe 0

        val runResult = container.execInContainer("./gradlew", "run")
        runResult.stdout shouldContain "Hello World!"
    }
})
