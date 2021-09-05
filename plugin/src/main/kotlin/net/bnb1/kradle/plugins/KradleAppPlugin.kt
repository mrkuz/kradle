package net.bnb1.kradle.plugins

import net.bnb1.kradle.apply
import net.bnb1.kradle.blueprints.ApplicationBlueprint
import net.bnb1.kradle.blueprints.JibBlueprint
import net.bnb1.kradle.blueprints.ShadowBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleBasePlugin::class.java)
        project.apply(BootstrapAppPlugin::class.java)
        project.apply(ApplicationBlueprint)
        project.apply(ShadowBlueprint)
        project.apply(JibBlueprint)
    }
}