package net.bnb1.kradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class KotlinLibraryTests : FunSpec({

    val container = TestContainer(this).also {
        it.bindResource("kotlin-lib.settings.gradle.kts", "settings.gradle.kts")
        it.bindResource("kotlin-lib.build.gradle.kts", "build.gradle.kts")
        it.start()
    }

    test("Install to local Maven repository") {
        container.get().execInContainer("rm", "-rf", ".m2/repository/com/example/kotlin-lib/")

        val bootstrapResult = container.exec("gradle", "bootstrap")
        bootstrapResult.exitCode shouldBe 0

        val installResult = container.exec("./gradlew", "install", "--info", "--stacktrace")
        installResult.exitCode shouldBe 0

        val lsResult = container.exec(
            "ls",
            ".m2/repository/com/example/kotlin-lib/1.0.0/kotlin-lib-1.0.0.jar"
        )
        lsResult.exitCode shouldBe 0
    }
})
