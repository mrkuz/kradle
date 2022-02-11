package net.bnb1.kradle.blueprints.jvm

import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.jaxrs.JerseyDockerHttpClient
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import java.util.*

class JibBlueprintTests : BehaviorSpec({

    val config = DefaultDockerClientConfig.createDefaultConfigBuilder().build()
    val httpClient = JerseyDockerHttpClient.Builder()
        .dockerHost(config.dockerHost)
        .sslConfig(config.sslConfig)
        .build()
    val dockerClient = DockerClientImpl.getInstance(config, httpClient)

    val name = "app-" + UUID.randomUUID()
    val project = TestProject(this)

    fun runContainer(): String {
        val container = dockerClient.createContainerCmd("$name:latest").exec().id
        dockerClient.startContainerCmd(container).exec()
        val waitCallback = WaitContainerResultCallback()
        dockerClient.waitContainerCmd(container).exec(waitCallback)
        waitCallback.awaitStatusCode()
        var output = ""
        dockerClient.logContainerCmd(container)
            .withStdOut(true)
            .withStdErr(true)
            .exec(object : ResultCallback.Adapter<Frame>() {
                override fun onNext(frame: Frame) {
                    output += String(frame.payload)
                    super.onNext(frame)
                }
            })
            .awaitCompletion()
        dockerClient.removeContainerCmd(container).exec()
        return output
    }

    afterEach {
        dockerClient.listImagesCmd().exec()
            .filter { it.repoTags.contains("$name:latest") }
            .forEach {
                println("Removing image ${it.id}")
                dockerClient.removeImageCmd(it.id).withForce(true).exec()
            }
    }

    Given("Default configuration") {
        project.setUp(name) {
            """
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
                docker.enable()
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Check for tasks") {

            Then("Task buildImage is available") {
                project.shouldHaveTask("buildImage")

                // And: "Task pushImage is available"
                project.shouldHaveTask("pushImage")
            }
        }

        // Requires docker
        xWhen("Run buildImage") {
            val result = project.runTask("buildImage")

            Then("Succeed") {
                result.task(":buildImage")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Docker image is available"
                val images = dockerClient.listImagesCmd().exec()
                images.forOne { it.repoTags.shouldContain("$name:latest") }
                images.forOne { it.repoTags.shouldContain("$name:1.0.0") }
            }
        }

        // Requires docker
        xWhen("Build and run container") {
            project.runTask("buildImage")
            val output = runContainer()

            Then("Succeed") {
                output shouldContain "Hello World"
            }
        }
    }

    // Requires docker
    xGiven("docker.baseImage = bellsoft/liberica-openjdk-alpine:11") {
        project.setUp(name) {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                docker {
                    baseImage("bellsoft/liberica-openjdk-alpine:11")
                }
            }
            """.trimIndent()
        }
        project.writeAppJava { "System.out.println(\"Java \" + System.getProperty(\"java.version\"));" }

        When("Build and run container") {
            project.runTask("buildImage")
            val output = runContainer()

            Then("Succeed") {
                output shouldContain "Java 11"
            }
        }
    }

    // Requires docker
    xGiven("docker.startupScript = true") {
        project.setUp(name) {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                docker {
                    startupScript(true)
                }
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppJava()

        When("Run buildImage") {
            project.runTask("buildImage")

            Then("app.sh is created") {
                project.projectDir.resolve("src/main/extra/app.sh").shouldExist()

                // And: "tini is downloaded"
                project.projectDir.resolve("src/main/extra/tini").shouldExist()
            }
        }

        When("Build and run container") {
            project.runTask("buildImage")
            val output = runContainer()

            Then("Succeed") {
                // app.sh output
                output shouldContain "exec java -XX:+ExitOnOutOfMemoryError"
                output shouldContain "Hello World"
            }
        }
    }

    // Requires docker
    xGiven("docker.withJvmKill()") {
        project.setUp(name) {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                docker {
                    withJvmKill("1.16.0")
                }
            }
            """.trimIndent()
        }
        project.writeAppJava { "System.out.println(ProcessHandle.current().info().commandLine().get());" }

        When("Run buildImage") {
            project.runTask("buildImage")

            Then("jvmkill is downloaded") {
                project.projectDir.resolve("src/main/extra/jvmkill-1.16.0.so").shouldExist()

                // And: "tini is downloaded"
                project.projectDir.resolve("src/main/extra/tini").shouldExist()
            }
        }

        When("Build and run container") {
            project.runTask("buildImage")
            val output = runContainer()

            Then("Succeed") {
                output shouldContain "-agentpath:/app/extra/jvmkill-1.16.0.so"
            }
        }
    }

    // Requires docker
    xGiven("docker.jvmOpts = -Xmx96M") {
        project.setUp(name) {
            """
            jvm {
                java.enable()
                application {
                    mainClass("com.example.demo.App")
                }
                docker {
                    jvmOpts("-Xmx96M")
                }
            }
            """.trimIndent()
        }
        project.writeAppJava { "System.out.println(\"Xmx = \" + Runtime.getRuntime().maxMemory() / 1024 / 1024);" }

        // Requires docker
        When("Build and run container") {
            project.runTask("buildImage")
            val output = runContainer()

            Then("Succeed") {
                output shouldContain "Xmx = 96"
            }
        }
    }
})
