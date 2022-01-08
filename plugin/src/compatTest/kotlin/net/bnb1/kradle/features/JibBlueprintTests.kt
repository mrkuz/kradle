package net.bnb1.kradle.features

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.core.DockerClientBuilder
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.CompatSpec
import java.util.*

class JibBlueprintTests : CompatSpec({

    val name = "app-" + UUID.randomUUID()

    afterEach {
        val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()
        dockerClient.listImagesCmd().exec()
            .filter { it.repoTags.contains("$name:latest") }
            .forEach {
                println("Removing image ${it.id}")
                dockerClient.removeImageCmd(it.id).withForce(true).exec()
            }
    }

    // Requires Docker running
    xtest("Build image") {
        writeSettingsGradle(name)
        writeCompatAppBuildFile(buildFile)
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()
        val images = dockerClient.listImagesCmd().exec()
        images.forOne { it.repoTags.shouldContain("$name:latest") }
        images.forOne { it.repoTags.shouldContain("$name:1.0.0") }
    }

    // Requires Docker running
    xtest("Run container") {
        writeSettingsGradle(name)
        writeCompatAppBuildFile(buildFile)
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()
        val container = dockerClient.createContainerCmd("$name:latest").exec().id
        dockerClient.startContainerCmd(container).exec()
        val waitCallback = WaitContainerResultCallback()
        dockerClient.waitContainerCmd(container).exec(waitCallback)
        waitCallback.awaitCompletion()
        var output = ""
        dockerClient.logContainerCmd(container).withStdOut(true).exec(object : ResultCallback.Adapter<Frame>() {
            override fun onNext(frame: Frame) {
                output += String(frame.payload)
                super.onNext(frame)
            }
        })
        dockerClient.removeContainerCmd(container).exec()

        output shouldContain "Hello World"
    }

    // Requires Docker running
    xtest("With app.sh") {
        writeSettingsGradle(name)
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                targetJvm("11")
                mainClass("com.example.demo.App")
                image {
                  withAppSh()
                }
            }
            
            """.trimIndent()
        )
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        projectDir.resolve("src/main/extra/app.sh").shouldExist()
    }

    // Requires Docker running
    xtest("With jvmkill") {
        writeSettingsGradle(name)
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.6.0"
               id("net.bitsandbobs.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                targetJvm("11")
                mainClass("com.example.demo.App")
                image {
                  withJvmKill("1.16.0")
                }
            }
            
            """.trimIndent()
        )
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        projectDir.resolve("src/main/extra/jvmkill-1.16.0.so").shouldExist()
    }
})
