package net.bnb1.kradle.blueprints

import com.google.cloud.tools.jib.gradle.JibExtension
import com.google.cloud.tools.jib.gradle.JibPlugin
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import net.bnb1.kradle.extraDir
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.kotlin.dsl.configure
import java.net.URL
import java.nio.file.Files

object JibBlueprint : PluginBlueprint<JibPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        val useJvmKill = extension.image.jvmKillVersion.isPresent
        val useAppSh = extension.image.useAppSh.get()

        var jvmKillFileName = if (useJvmKill) {
            "jvmkill-${extension.image.jvmKillVersion.get()}.so"
        } else {
            ""
        }

        project.tasks.named("jibDockerBuild").configure {
            doFirst {
                if (useAppSh) {
                    createAppSh(project)
                }
                if (useJvmKill) {
                    downloadJvmKill(project, extension.image.jvmKillVersion.get(), jvmKillFileName)
                }
            }
        }

        project.configure<JibExtension> {
            from {
                image = extension.image.baseImage.get()
            }

            to {
                image = "${project.rootProject.name}:latest"
                tags = setOf(project.version.toString())
            }

            container {
                creationTime = "USE_CURRENT_TIMESTAMP"
                if (extension.image.ports.isPresent) {
                    ports = extension.image.ports.get().map { it.toString() }
                }

                if (extension.image.javaOpts.isPresent) {
                    if (useAppSh) {
                        environment = mapOf("JAVA_OPTS" to extension.image.javaOpts.get())
                    } else {
                        jvmFlags = extension.image.javaOpts.get().split(" ")
                    }
                }

                if (useJvmKill) {
                    if (useAppSh) {
                        environment = environment + mapOf("JAVA_AGENT" to "/app/extra/${jvmKillFileName}")
                    } else {
                        jvmFlags = jvmFlags + listOf("-agentpath:/app/extra/${jvmKillFileName}")
                    }
                }

                if (useAppSh) {
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

        project.alias("buildImage", "Builds Docker image", "jibDockerBuild")
    }

    private fun downloadJvmKill(project: Project, jvmKillVersion: String, jvmKillFileName: String) {
        val jvmKillFile = project.extraDir.resolve("$jvmKillFileName")
        if (jvmKillFile.exists()) {
            return
        }

        jvmKillFile.parentFile.mkdirs()

        val jvmKillBaseUrl = "https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64"
        val url = URL(jvmKillBaseUrl + "/jvmkill-${jvmKillVersion}-RELEASE.so")
        project.logger.lifecycle("Downloading $url")
        url.openStream().use { Files.copy(it, jvmKillFile.toPath()) }
    }

    private fun createAppSh(project: Project) {
        val file = project.rootDir.resolve("extra/app.sh")
        if (file.exists()) {
            return
        }

        file.parentFile.mkdirs()

        val javaExtension = project.extensions.getByType(JavaApplication::class.java)
        val mainClass = javaExtension.mainClass.get()

        file.writeText(
            """
            # !/bin/sh -e
            if [ "${'$'}{JAVA_URANDOM:-true}" = "true" ]; then
                JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom ${'$'}JAVA_OPTS"
            fi
            if [ "${'$'}JAVA_DIAGNOSTICS" = "true" ]; then
                JAVA_OPTS="-XX:+UnlockDiagnosticVMOptions -XX:+PrintFlagsFinal -Xlog:gc ${'$'}JAVA_OPTS"
            fi
            if [ "${'$'}JAVA_DEBUG" = "true" ]; then
                JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${'$'}{JAVA_DEBUG_PORT:-8000} ${'$'}JAVA_OPTS"
            fi
            if [ -n "${'$'}JAVA_AGENT" ]; then
                JAVA_OPTS="-agentpath:${'$'}JAVA_AGENT ${'$'}JAVA_OPTS"
            fi
            JAVA_OPTS="-XX:+ExitOnOutOfMemoryError ${'$'}JAVA_OPTS"

            echo exec java ${'$'}JAVA_OPTS -cp @/app/jib-classpath-file $mainClass "${'$'}@"
            exec java ${'$'}JAVA_OPTS -cp @/app/jib-classpath-file $mainClass "${'$'}@"
        """.trimIndent()
        )
    }
}