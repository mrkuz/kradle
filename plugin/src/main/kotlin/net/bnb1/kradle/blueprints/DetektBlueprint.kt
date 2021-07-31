package net.bnb1.kradle.blueprints

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

object DetektBlueprint : PluginBlueprint<DetektPlugin> {

    override fun configure(project: Project) {
        project.configure<DetektExtension> {
            reports {
                html.enabled = true
                xml.enabled = false
                sarif.enabled = false
                txt.enabled = false
            }
        }
    }
}