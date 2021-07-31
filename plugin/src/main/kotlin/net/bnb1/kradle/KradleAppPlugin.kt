package net.bnb1.kradle

import net.bnb1.kradle.blueprints.ApplicationBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleBasePlugin::class.java)
        project.apply(ApplicationBlueprint)
    }
}