package net.bnb1.kradle.blueprints

import com.google.cloud.tools.jib.gradle.JibExtension
import com.google.cloud.tools.jib.gradle.JibPlugin
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.net.URL
import java.nio.file.Files

object JibBlueprint : PluginBlueprint<JibPlugin> {

    override fun configure(project: Project) {
        project.afterEvaluate {
            val extension = project.extensions.getByType(KradleExtension::class.java).image
            val useJvmKill = extension.jvmKillVersion.isPresent

            lateinit var jvmKillFileName: String

            if (useJvmKill) {
                jvmKillFileName = "jvmkill-${extension.jvmKillVersion.get()}.so"
                project.tasks.named("jibDockerBuild").configure {
                    doFirst {
                        downloadJvmKill(project, extension.jvmKillVersion.get(), jvmKillFileName)
                    }
                }
            }

            project.configure<JibExtension> {

                from {
                    image = extension.baseImage.get()
                }

                to {
                    image = "${project.rootProject.name}:latest"
                    tags = setOf(project.version.toString())
                }

                container {
                    creationTime = "USE_CURRENT_TIMESTAMP"
                    if (extension.ports.isPresent) {
                        ports = extension.ports.get().map { it.toString() }
                    }

                    if (useJvmKill) {
                        jvmFlags = jvmFlags + listOf("-agentpath:/app/extra/${jvmKillFileName}")
                    }
                }

                if (useJvmKill) {
                    extraDirectories {
                        paths {
                            path {
                                setFrom("${project.rootDir}/extra/")
                                into = "/app/extra"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadJvmKill(project: Project, jvmKillVersion: String, jvmKillFileName: String) {
        val jvmKillFile = project.rootDir.resolve("extra/${jvmKillFileName}")
        if (jvmKillFile.exists()) {
            return
        }

        jvmKillFile.parentFile.mkdirs()

        val jvmKillBaseUrl = "https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64"
        val url = URL(jvmKillBaseUrl + "/jvmkill-${jvmKillVersion}-RELEASE.so")
        project.logger.lifecycle("Downloading $url")
        url.openStream().use { Files.copy(it, jvmKillFile.toPath()) }
    }
}