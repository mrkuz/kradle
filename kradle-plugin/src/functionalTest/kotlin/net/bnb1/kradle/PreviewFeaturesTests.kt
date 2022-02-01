package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PreviewFeaturesTests : FunSpec({

    val container = TestContainer(this).also {
        it.bindResource("settings.gradle.kts")
        it.bindResource("preview.build.gradle.kts", "build.gradle.kts")
        it.bindResource("preview.App.java", "src/main/java/com/example/demo/App.java")
        it.start()
    }

    test("Use Java preview features") {
        val bootstrapResult = container.exec("gradle", "bootstrap")
        bootstrapResult.exitCode shouldBe 0

        val installResult = container.exec("./gradlew", "compileJava")
        installResult.exitCode shouldBe 0
    }
})
