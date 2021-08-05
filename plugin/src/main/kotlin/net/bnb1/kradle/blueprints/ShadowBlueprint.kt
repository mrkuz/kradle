package net.bnb1.kradle.blueprints

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

object ShadowBlueprint : PluginBlueprint<ShadowPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.tasks.named<ShadowJar>("shadowJar").configure {
            archiveClassifier.set("uber")
            // Remove unused dependencies
            minimize()
        }

        project.alias("uberJar", "Creates Uber-JAR", "shadowJar")
    }
}