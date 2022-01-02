package net.bnb1.kradle.features.jvm

import com.google.cloud.tools.jib.gradle.BuildDockerTask
import com.google.cloud.tools.jib.gradle.JibExtension
import net.bnb1.kradle.*
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.named
import java.net.URL
import java.nio.file.Files

private const val TASK_NAME = "buildImage"

class JibBlueprint(project: Project) : Blueprint(project) {

    override fun checkPreconditions() {
        if (!project.featureRegistry.get<ApplicationFeature>().isEnabled) {
            throw GradleException("'docker' requires 'application' feature")
        }
    }

    override fun createTasks() {
        project.createTask<BuildDockerTask>(TASK_NAME, "Builds Docker image")
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<DockerProperties>()

        project.tasks.named<BuildDockerTask>(TASK_NAME).configure {
            dependsOn(project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).runtimeClasspath)
            dependsOn(project.configurations.getByName("runtimeClasspath"))
            setJibExtension(createExtension())
            doFirst {
                if (properties.withAppSh.get()) {
                    createAppSh(project)
                }
                if (properties.jvmKillVersion.hasValue) {
                    downloadJvmKill(project)
                }
            }
        }
    }

    private fun createExtension(): JibExtension {
        val properties = project.propertiesRegistry.get<DockerProperties>()
        val jibExtension = JibExtension(project).apply {
            from {
                image = properties.baseImage.get()
            }

            to {
                image = "${project.rootProject.name}:latest"
                tags = setOf(project.version.toString())
            }

            container {
                creationTime = "USE_CURRENT_TIMESTAMP"
                if (properties.ports.isPresent) {
                    ports = properties.ports.get().map { it.toString() }
                }

                if (properties.javaOpts.hasValue) {
                    if (properties.withAppSh.get()) {
                        environment = mapOf("JAVA_OPTS" to properties.javaOpts.get())
                    } else {
                        jvmFlags = properties.javaOpts.get().split(" ")
                    }
                }

                if (properties.jvmKillVersion.hasValue) {
                    val jvmKillFileName = "jvmkill-${properties.jvmKillVersion.get()}.so"
                    if (properties.withAppSh.get()) {
                        environment = environment + mapOf("JAVA_AGENT" to "/app/extra/$jvmKillFileName")
                    } else {
                        jvmFlags = jvmFlags + listOf("-agentpath:/app/extra/$jvmKillFileName")
                    }
                }

                if (properties.withAppSh.get()) {
                    val mainClass = project.propertiesRegistry.get<ApplicationProperties>().mainClass.get()
                    environment = environment + mapOf("MAIN_CLASS" to mainClass)
                    entrypoint = listOf("/bin/sh", "/app/extra/app.sh")
                }
            }

            if (project.extraDir.exists()) {
                extraDirectories {
                    paths {
                        path {
                            setFrom(project.extraDir)
                            into = "/app/extra"
                        }
                        permissions = mapOf("**/*.sh" to "755")
                    }
                }
            }
        }
        return jibExtension
    }

    private fun createAppSh(project: Project) {
        val file = project.extraDir.resolve("app.sh")
        if (file.exists()) {
            return
        }

        file.parentFile.mkdirs()
        file.writeText(javaClass.getResource("/app.sh")!!.readText())
    }

    private fun downloadJvmKill(project: Project) {
        val properties = project.propertiesRegistry.get<DockerProperties>()
        val jvmKillFile = project.extraDir.resolve("jvmkill-${properties.jvmKillVersion.get()}.so")
        if (jvmKillFile.exists()) {
            return
        }

        jvmKillFile.parentFile.mkdirs()

        val jvmKillBaseUrl = "https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64"
        val url = URL(jvmKillBaseUrl + "/jvmkill-${properties.jvmKillVersion.get()}-RELEASE.so")
        project.logger.lifecycle("Downloading $url")
        url.openStream().use { Files.copy(it, jvmKillFile.toPath()) }
    }
}
