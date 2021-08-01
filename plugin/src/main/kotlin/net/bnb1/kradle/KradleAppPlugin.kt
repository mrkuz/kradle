package net.bnb1.kradle

import net.bnb1.kradle.blueprints.ApplicationBlueprint
import net.bnb1.kradle.blueprints.ShadowBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleBasePlugin::class.java)
        project.apply(ApplicationBlueprint)
        project.apply(ShadowBlueprint)

        project.alias("uberJar", "Creates Uber-JAR", "shadowJar")
    }
}