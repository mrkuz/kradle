package net.bnb1.kradle.features.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.support.plugins.BuildPropertiesPlugin
import org.gradle.api.Project

class BuildPropertiesBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(BuildPropertiesPlugin::class.java)
    }

    override fun configure() {
        project.tasks.named("processResources").configure {
            finalizedBy("generateBuildProperties")
        }
    }
}
