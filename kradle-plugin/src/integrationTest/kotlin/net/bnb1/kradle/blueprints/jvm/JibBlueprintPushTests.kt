package net.bnb1.kradle.blueprints.jvm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

private const val REGISTRY_PORT = 5000

class JibBlueprintPushTests : BehaviorSpec({

    val container = KGenericContainer(DockerImageName.parse("registry:2"))
        .withExposedPorts(REGISTRY_PORT)

    beforeSpec {
        container.start()
    }

    afterSpec {
        container.stop()
    }

    val project = TestProject(this)

    Given("docker.imageName = localhost:5000/demo") {
        val registry = "${container.host}:${container.getMappedPort(REGISTRY_PORT)}"
        project.setUp {
            """
            jvm {
                kotlin.enable()
                application {
                    mainClass("com.example.demo.AppKt")
                }
                docker {
                    imageName("$registry/demo")
                    allowInsecureRegistries(true)
                }
            }
            """.trimIndent()
        }
        project.writeHelloWorldAppKt()

        When("Run pushImage") {
            val result = project.runTask("pushImage")

            Then("Succeed") {
                result.task(":pushImage")!!.outcome shouldBe TaskOutcome.SUCCESS
            }
        }
    }
})

class KGenericContainer(dockerImageName: DockerImageName) : GenericContainer<KGenericContainer>(dockerImageName)
