package net.bnb1.kradle.blueprints

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import net.bnb1.kradle.execute

class ShadowBlueprintTests : PluginSpec({

    test("Create uber JAR") {
        bootstrapAppProject()

        runTask("uberJar")

        buildDir.resolve("libs/app-1.0.0-uber.jar").shouldExist()
    }

    test("Create and run uber JAR") {
        bootstrapAppProject()
        writeAppKt("println(\"Hello World\")")

        runTask("uberJar")

        val jarFile = buildDir.resolve("libs/app-1.0.0-uber.jar")
        jarFile.shouldExist()
        "java -jar ${jarFile.absolutePath}".execute() shouldContain "Hello World"
    }
})