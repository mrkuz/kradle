package net.bnb1.kradle.features

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec
import net.bnb1.kradle.execute

class ShadowBlueprintTests : CompatSpec({

    test("Create uber JAR") {
        bootstrapCompatAppProject()

        runTask("uberJar")

        buildDir.resolve("libs/app-1.0.0-uber.jar").shouldExist()
    }

    test("Create and run uber JAR") {
        bootstrapCompatAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("uberJar")

        val jarFile = buildDir.resolve("libs/app-1.0.0-uber.jar")
        jarFile.shouldExist()
        "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"
    }
})
