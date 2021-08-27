package net.bnb1.kradle.blueprints

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import net.bnb1.kradle.create
import net.bnb1.kradle.tasks.GenerateDetektConfigTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

object DetektBlueprint : PluginBlueprint<DetektPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        val configFile = project.rootDir.resolve(extension.detektConfigFile.get())
        project.configure<DetektExtension> {
            if (configFile.exists()) {
                buildUponDefaultConfig = false
                config.setFrom(configFile)
            }
            reports {
                html.enabled = true
                xml.enabled = false
                sarif.enabled = false
                txt.enabled = false
            }
        }

        project.create("generateDetektConfig", "Generates detekt-config.yml", GenerateDetektConfigTask::class.java)
        project.alias("analyzeCode", "Runs detekt code analysis", "detekt")
    }
}