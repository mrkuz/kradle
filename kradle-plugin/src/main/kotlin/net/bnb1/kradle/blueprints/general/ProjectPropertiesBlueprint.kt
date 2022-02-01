package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.support.plugins.ProjectPropertiesPlugin
import org.gradle.api.Project

class ProjectPropertiesBlueprint(project: Project) : Blueprint(project) {

    override fun doApplyPlugins() {
        project.apply(ProjectPropertiesPlugin::class.java)
    }
}
