package net.bnb1.kradle.blueprints

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.core.DockerClientBuilder
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.PluginSpec
import java.util.*

class JibBlueprintTests : PluginSpec({

    val name = "app-" + UUID.randomUUID()

    afterEach {
        val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()
        dockerClient.listImagesCmd().exec()
            .filter { it.repoTags.contains("${name}:latest") }
            .forEach {
                println("Removing image ${it.id}")
                dockerClient.removeImageCmd(it.id).withForce(true).exec()
            }
    }

    // Requires Docker running
    xtest("Build image") {
        writeSettingsGradle(name)
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                targetJvm.set("11")
            }
            
            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()
        val images = dockerClient.listImagesCmd().exec()
        images.forOne { it.repoTags.shouldContain("${name}:latest") }
        images.forOne { it.repoTags.shouldContain("${name}:1.0.0") }
    }

    // Requires Docker running
    xtest("Run container") {
        writeSettingsGradle(name)
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                targetJvm.set("11")
            }
            
            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()
        val container = dockerClient.createContainerCmd("${name}:latest").exec().id
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
    xtest("Use app.sh") {
        writeSettingsGradle(name)
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                targetJvm.set("11")
                image {
                  useAppSh()
                }
            }
            
            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        projectDir.resolve("extra/app.sh").shouldExist()
    }

    // Requires Docker running
    xtest("Use jvmkill") {
        writeSettingsGradle(name)
        buildFile.writeText(
            """
            plugins {
               id("org.jetbrains.kotlin.jvm") version "1.4.31"
               id("net.bnb1.kradle-app") version "main-SNAPSHOT"
            }
            
            group = "com.example"
            version = "1.0.0"

            kradle {
                targetJvm.set("11")
                image {
                  useJvmKill()
                }
            }
            
            application {
                mainClass.set("com.example.AppKt")
            }
            
            """.trimIndent()
        )
        writeAppKt("println(\"Hello World\")")

        runTask("buildImage")

        projectDir.resolve("extra/jvmkill-1.16.0.so").shouldExist()
    }
})