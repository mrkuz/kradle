package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.plugins.BuildPropertiesPlugin
import org.gradle.api.Project

object BuildPropertiesBlueprint : PluginBlueprint<BuildPropertiesPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.tasks.named("processResources").configure {
            finalizedBy("generateBuildProperties")
        }
    }
}
