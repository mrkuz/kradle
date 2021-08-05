package net.bnb1.kradle.plugins

import net.bnb1.kradle.alias
import net.bnb1.kradle.apply
import net.bnb1.kradle.blueprints.MavenPublishBlueprint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

class KradleLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleBasePlugin::class.java)
        project.apply(JavaLibraryPlugin::class.java)

        project.apply(MavenPublishBlueprint)
        project.alias("install", "Installs JAR to local Maven repository", "publishToMavenLocal")
    }
}