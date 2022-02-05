package net.bnb1.kradle.blueprints.jvm

import com.google.cloud.tools.jib.gradle.BuildDockerTask
import com.google.cloud.tools.jib.gradle.JibExtension
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import net.bnb1.kradle.extraDir
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.named
import java.net.URL
import java.nio.file.Files

private const val TASK_NAME = "buildImage"

class JibBlueprint(project: Project) : Blueprint(project) {

    lateinit var dockerProperties: DockerProperties
    lateinit var applicationProperties: ApplicationProperties

    override fun doCreateTasks() {
        project.createTask<BuildDockerTask>(TASK_NAME, "Builds Docker image")
    }

    override fun doConfigure() {
        project.tasks.named<BuildDockerTask>(TASK_NAME).configure {
            dependsOn(project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).runtimeClasspath)
            dependsOn(project.configurations.getByName("runtimeClasspath"))
            setJibExtension(createExtension())
            doFirst {
                if (dockerProperties.withStartupScript.get()) {
                    createAppSh(project)
                }
                if (dockerProperties.withJvmKill.hasValue) {
                    downloadJvmKill(project)
                }
            }
        }
    }

    private fun createExtension(): JibExtension {
        val jibExtension = JibExtension(project).apply {
            from {
                image = dockerProperties.baseImage.get()
            }

            to {
                image = "${project.rootProject.name}:latest"
                tags = setOf(project.version.toString())
            }

            container {
                creationTime = "USE_CURRENT_TIMESTAMP"
                ports = dockerProperties.ports.get().map { it.toString() }

                if (dockerProperties.javaOpts.hasValue) {
                    if (dockerProperties.withStartupScript.get()) {
                        environment = mapOf("JAVA_OPTS" to dockerProperties.javaOpts.get())
                    } else {
                        jvmFlags = dockerProperties.javaOpts.get().split(" ")
                    }
                }

                if (dockerProperties.withJvmKill.hasValue) {
                    val jvmKillFileName = "jvmkill-${dockerProperties.withJvmKill.get()}.so"
                    if (dockerProperties.withStartupScript.get()) {
                        environment = environment + mapOf("JAVA_AGENT" to "/app/extra/$jvmKillFileName")
                    } else {
                        jvmFlags = jvmFlags + listOf("-agentpath:/app/extra/$jvmKillFileName")
                    }
                }

                if (dockerProperties.withStartupScript.get()) {
                    val mainClass = applicationProperties.mainClass.get()
                    environment = environment + mapOf("MAIN_CLASS" to mainClass)
                    entrypoint = listOf("/bin/sh", "/app/extra/app.sh")
                }
            }

            if (project.extraDir.exists() ||
                dockerProperties.withStartupScript.get() ||
                dockerProperties.withJvmKill.hasValue
            ) {
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
        val jvmKillFile = project.extraDir.resolve("jvmkill-${dockerProperties.withJvmKill.get()}.so")
        if (jvmKillFile.exists()) {
            return
        }

        jvmKillFile.parentFile.mkdirs()

        val jvmKillBaseUrl = "https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64"
        val url = URL(jvmKillBaseUrl + "/jvmkill-${dockerProperties.withJvmKill.get()}-RELEASE.so")
        project.logger.lifecycle("Downloading $url")
        url.openStream().use { Files.copy(it, jvmKillFile.toPath()) }
    }
}
