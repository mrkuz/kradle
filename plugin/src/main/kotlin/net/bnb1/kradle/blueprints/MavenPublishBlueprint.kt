package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.configure

object MavenPublishBlueprint : PluginBlueprint<MavenPublishPlugin> {

    override fun configureEager(project: Project) {
        project.configure<PublishingExtension> {
            publications {
                create("default", MavenPublication::class.java) {
                    artifactId = project.rootProject.name
                    from(project.components.getByName("java"))
                }
            }
        }

        project.alias("install", "Installs JAR to local Maven repository", "publishToMavenLocal")
    }
}
