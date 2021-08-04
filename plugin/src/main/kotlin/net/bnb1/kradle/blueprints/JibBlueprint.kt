package net.bnb1.kradle.blueprints

import com.google.cloud.tools.jib.gradle.JibExtension
import com.google.cloud.tools.jib.gradle.JibPlugin
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

object JibBlueprint : PluginBlueprint<JibPlugin> {

    override fun configure(project: Project) {
        project.afterEvaluate {
            val extension = project.extensions.getByType(KradleExtension::class.java)
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
                }
            }
        }
    }
}