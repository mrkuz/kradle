package net.bnb1.kradle.features.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.plugins.ProjectPropertiesPlugin
import org.gradle.api.Project

class ProjectPropertiesBlueprint(project : Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(ProjectPropertiesPlugin::class.java)
    }
}
