package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.support.plugins.BuildPropertiesPlugin
import org.gradle.api.Project

class BuildPropertiesBlueprint(project: Project) : Blueprint(project) {

    override fun doApplyPlugins() {
        project.apply(BuildPropertiesPlugin::class.java)
    }

    override fun doConfigure() {
        project.tasks.named("processResources").configure {
            finalizedBy("generateBuildProperties")
        }
    }
}
