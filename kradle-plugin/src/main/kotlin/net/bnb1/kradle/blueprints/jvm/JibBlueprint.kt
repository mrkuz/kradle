package net.bnb1.kradle.blueprints.jvm

import com.google.cloud.tools.jib.gradle.BuildDockerTask
import com.google.cloud.tools.jib.gradle.BuildImageTask
import com.google.cloud.tools.jib.gradle.JibExtension
import com.google.cloud.tools.jib.gradle.JibTask
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import net.bnb1.kradle.extraDir
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.extra
import java.net.URL
import java.nio.file.Files

private const val TASK_BUILD_IMAGE = "buildImage"
private const val TASK_PUSH_IMAGE = "pushImage"

class JibBlueprint(project: Project) : Blueprint(project) {

    lateinit var dockerProperties: DockerProperties
    lateinit var applicationProperties: ApplicationProperties

    override fun doAddExtraProperties() {
        project.extra["imageName"] = dockerProperties.imageName ?: project.rootProject.name
    }

    override fun doCreateTasks() {
        val extension = createExtension()
        project.createTask<BuildDockerTask>(TASK_BUILD_IMAGE, "Builds Docker image")
            .also { configureTask(it, extension) }
        project.createTask<BuildImageTask>(TASK_PUSH_IMAGE, "Pushes container image to remote registry")
            .also { configureTask(it, extension) }
    }

    private fun configureTask(task: JibTask, extension: JibExtension) = task.apply {
        dependsOn(project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).runtimeClasspath)
        dependsOn(project.configurations.getByName("runtimeClasspath"))
        setJibExtension(extension)
        doFirst {
            if (dockerProperties.withStartupScript) {
                copyResource(project, "app.sh")
                downloadTini(project)
            }
            if (dockerProperties.withJvmKill != null) {
                downloadTini(project)
                downloadJvmKill(project)
            }
        }
    }

    @Suppress("LongMethod", "ComplexMethod")
    private fun createExtension(): JibExtension {
        val withJvmKill = dockerProperties.withJvmKill != null
        val withStartupScript = dockerProperties.withStartupScript
        val jibExtension = JibExtension(project).apply {
            setAllowInsecureRegistries(dockerProperties.allowInsecureRegistries)

            from {
                image = dockerProperties.baseImage
            }

            to {
                image = project.extra["imageName"].toString()
                tags = setOf(project.version.toString())
            }

            container {
                creationTime = "USE_CURRENT_TIMESTAMP"
                ports = dockerProperties.ports.map { it.toString() }

                if (dockerProperties.jvmOpts != null) {
                    if (withStartupScript) {
                        environment = mapOf("JAVA_OPTS" to dockerProperties.jvmOpts)
                    } else {
                        jvmFlags = dockerProperties.jvmOpts!!.split(" ")
                    }
                }

                if (withJvmKill) {
                    val jvmKillFileName = "jvmkill-${dockerProperties.withJvmKill}.so"
                    if (withStartupScript) {
                        environment = environment + mapOf("JAVA_AGENT" to "/app/extra/$jvmKillFileName")
                    } else {
                        jvmFlags = jvmFlags + listOf("-agentpath:/app/extra/$jvmKillFileName")
                    }
                }

                val mainClass = applicationProperties.mainClass
                if (withStartupScript) {
                    environment = environment + mapOf("MAIN_CLASS" to mainClass)
                    entrypoint = listOf("/app/extra/tini", "--", "/app/extra/app.sh")
                } else if (withJvmKill) {
                    entrypoint = listOf(
                        "/app/extra/tini",
                        "--",
                        "java"
                    ) + jvmFlags + listOf(
                        "-cp",
                        "@/app/jib-classpath-file",
                        mainClass
                    )
                }
            }

            if (project.extraDir.exists() || withStartupScript || withJvmKill) {
                extraDirectories {
                    paths {
                        path {
                            setFrom(project.extraDir)
                            into = "/app/extra"
                        }
                        permissions = mapOf(
                            "**/*.sh" to "755",
                            "**/tini" to "755"
                        )
                    }
                }
            }
        }
        return jibExtension
    }

    private fun copyResource(project: Project, name: String) {
        val file = project.extraDir.resolve(name)
        if (file.exists()) {
            return
        }

        file.parentFile.mkdirs()
        file.writeText(javaClass.getResource("/$name")!!.readText())
    }

    private fun downloadTini(project: Project) {
        val tiniFile = project.extraDir.resolve("tini")
        if (tiniFile.exists()) {
            return
        }

        tiniFile.parentFile.mkdirs()

        val url = URL("https://github.com/krallin/tini/releases/download/v${Catalog.Versions.tini}/tini")
        project.logger.lifecycle("Downloading $url")
        url.openStream().use { Files.copy(it, tiniFile.toPath()) }
    }

    private fun downloadJvmKill(project: Project) {
        val jvmKillFile = project.extraDir.resolve("jvmkill-${dockerProperties.withJvmKill}.so")
        if (jvmKillFile.exists()) {
            return
        }

        jvmKillFile.parentFile.mkdirs()

        val jvmKillBaseUrl = "https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64"
        val url = URL(jvmKillBaseUrl + "/jvmkill-${dockerProperties.withJvmKill}-RELEASE.so")
        project.logger.lifecycle("Downloading $url")
        url.openStream().use { Files.copy(it, jvmKillFile.toPath()) }
    }
}
