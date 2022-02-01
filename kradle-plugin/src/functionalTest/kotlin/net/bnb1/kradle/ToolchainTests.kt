package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ToolchainTests : FunSpec({

    val container = TestContainer(this).also {
        it.bindResource("settings.gradle.kts")
        it.bindResource("toolchain.build.gradle.kts", "build.gradle.kts")
        it.bindResource("toolchain.App.kt", "src/main/kotlin/com/example/demo/App.kt")
        it.start()
    }

    test("Use Gradle toolchain") {
        val bootstrapResult = container.exec("gradle", "bootstrap")
        bootstrapResult.exitCode shouldBe 0

        val installResult = container.exec("./gradlew", "run")
        installResult.exitCode shouldBe 0
        installResult.stdout shouldContain "Java 11"
    }
})
