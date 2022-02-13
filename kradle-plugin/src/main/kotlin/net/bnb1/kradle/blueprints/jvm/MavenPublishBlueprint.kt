package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.alias
import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.configure

class MavenPublishBlueprint(project: Project) : Blueprint(project) {

    override fun doApplyPlugins() {
        project.apply(MavenPublishPlugin::class.java)
    }

    override fun doAddAliases() {
        project.alias("install", "Installs JAR to local Maven repository", "publishToMavenLocal")
    }

    override fun doConfigure() {
        project.configure<PublishingExtension> {
            publications {
                create("default", MavenPublication::class.java) {
                    artifactId = project.name
                    from(project.components.getByName("java"))
                }
            }
        }
    }
}
