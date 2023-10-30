package net.bnb1.kradle.blueprints.general

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import net.bnb1.kradle.TestProject
import org.gradle.testkit.runner.TaskOutcome

class HelmBlueprintTests : BehaviorSpec({

    val project = TestProject(this)

    Given("Default configuration") {
        project.setUp("app") {
            """
            general {
                helm.enable()
            }
            """
        }

        When("Check for tasks") {

            Then("Task generateHelmChart is available") {
                project.shouldHaveTask("generateHelmChart")

                // And:"Task processHelmChart is available"
                project.shouldHaveTask("processHelmChart")

                // And:"Task helmInstall is available"
                project.shouldHaveTask("helmInstall")

                // And:"Task helmUpgrade is available"
                project.shouldHaveTask("helmUpgrade")

                // And:"Task helmUninstall is available"
                project.shouldHaveTask("helmUninstall")
            }
        }

        When("Run generateHelmChart") {
            val result = project.runTask("generateHelmChart")

            Then("Succeed") {
                result.task(":generateHelmChart")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Helm chart exists"
                project.projectDir.resolve("src/main/helm/Chart.yaml").shouldExist()
                project.projectDir.resolve("src/main/helm/values.yaml").shouldExist()
                project.projectDir.resolve("src/main/helm/.helmignore").shouldExist()
                project.projectDir.resolve("src/main/helm/templates/_helpers.tpl").shouldExist()
                project.projectDir.resolve("src/main/helm/templates/deployment.yaml").shouldExist()
                project.projectDir.resolve("src/main/helm/templates/ingress.yaml").shouldExist()
                project.projectDir.resolve("src/main/helm/templates/NOTES.txt").shouldExist()
                project.projectDir.resolve("src/main/helm/templates/service.yaml").shouldExist()
                project.projectDir.resolve("src/main/helm/templates/serviceaccount.yaml").shouldExist()
                project.projectDir.resolve("src/main/helm/charts/.gitkeep").shouldExist()
            }
        }
    }

    Given("helm.releaseName = demo-beta") {
        project.setUp("app") {
            """
            general {
                helm {
                    releaseName("app-beta")
                }
            }
            """
        }

        When("Run helmInstall") {
            val result = project.runTask("helmInstall", "--echo")

            Then("Release name is used") {
                result.output.shouldContain("helm install app-beta")
            }
        }

        When("Run helmUpgrade") {
            val result = project.runTask("helmUpgrade", "--echo")

            Then("Release name is used") {
                result.output.shouldContain("helm upgrade --install app-beta")
            }
        }

        When("Run helmUninstall") {
            val result = project.runTask("helmUninstall", "--echo")

            Then("Release name is used") {
                result.output.shouldContain("helm uninstall app-beta")
            }
        }
    }

    Given("helm.valuesFile = values-test.yaml") {
        project.setUp("app") {
            """
            general {
                helm {
                    valuesFile("values-test.yaml")
                }
            }
            """
        }

        When("Run helmInstall") {
            val result = project.runTask("helmInstall", "--echo")

            Then("Values file is used") {
                result.output.shouldContain(
                    "helm install app " +
                        "${project.buildDir.resolve("helm").canonicalPath} " +
                        "-f ${project.projectDir.resolve("values-test.yaml").canonicalPath}"
                )
            }
        }

        When("Run helmUpgrade") {
            val result = project.runTask("helmUpgrade", "--echo")

            Then("Values file is used") {
                result.output.shouldContain(
                    "helm upgrade --install app " +
                        "${project.buildDir.resolve("helm").canonicalPath} " +
                        "-f ${project.projectDir.resolve("values-test.yaml").canonicalPath}"
                )
            }
        }
    }

    Given("Default configuration and docker is enabled") {
        project.setUp("app") {
            """
            general {
                helm.enable()
            }
            jvm {
                application {
                    mainClass("com.example.demo.AppKt")
                }
                docker.enable {
                    imageName("app-image")
                }
            }
            """
        }
        project.runTask("generateHelmChart")

        When("Run processHelmChart") {
            val result = project.runTask("processHelmChart")

            Then("Succeed") {
                result.task(":processHelmChart")!!.outcome shouldBe TaskOutcome.SUCCESS

                // And: "Output Helm chart exists"
                val chart = project.buildDir.resolve("helm/Chart.yaml")
                chart.shouldExist()
                val values = project.buildDir.resolve("helm/values.yaml")
                values.shouldExist()
                project.buildDir.resolve("helm/.helmignore").shouldExist()
                project.buildDir.resolve("helm/templates/_helpers.tpl").shouldExist()
                project.buildDir.resolve("helm/templates/deployment.yaml").shouldExist()
                project.buildDir.resolve("helm/templates/ingress.yaml").shouldExist()
                project.buildDir.resolve("helm/templates/NOTES.txt").shouldExist()
                project.buildDir.resolve("helm/templates/service.yaml").shouldExist()
                project.buildDir.resolve("helm/templates/serviceaccount.yaml").shouldExist()
                project.buildDir.resolve("helm/charts/.gitkeep").shouldExist()

                // And: "Properties in Chart.yaml are expanded"
                var lines = chart.readLines()
                lines.forOne { it shouldBe "name: app" }
                lines.forOne { it shouldBe "version: 1.0.0" }
                lines.forOne { it shouldBe "appVersion: \"1.0.0\"" }

                // And: "Properties in values.yaml are expanded"
                lines = values.readLines()
                lines.forOne { it shouldBe "  repository: app-image" }
            }
        }
    }
})
