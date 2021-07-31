package net.bnb1.kradle

import net.bnb1.kradle.blueprints.MavenPublishBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleBasePlugin::class.java)

        project.apply(MavenPublishBlueprint)
        project.alias("install", "Installs JAR to local Maven repository", "publishToMavenLocal")
    }
}