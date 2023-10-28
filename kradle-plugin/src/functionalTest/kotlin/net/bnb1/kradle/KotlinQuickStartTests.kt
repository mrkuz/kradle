package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class KotlinQuickStartTests : FunSpec({

    val container = TestContainer(this).start()

    test("(Very) Quick Start") {
        container.exec(
            "curl",
            "-O",
            "https://raw.githubusercontent.com/mrkuz/kradle/main/examples/kotlin/app/settings.gradle.kts"
        )
        container.exec(
            "curl",
            "-O",
            "https://raw.githubusercontent.com/mrkuz/kradle/main/examples/kotlin/app/build.gradle.kts"
        )

        val bootstrapResult = container.exec("gradle", "bootstrap")
        bootstrapResult.exitCode shouldBe 0

        val runResult = container.exec("./gradlew", "run", "--info", "--stacktrace")
        runResult.stdout shouldContain "Hello World!"
    }
})
