package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.plugins.BootstrapAppPlugin
import net.bnb1.kradle.plugins.BootstrapLibPlugin
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.tasks.BootstrapAppTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class KotlinBootstrapBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        with(project.featureRegistry) {
            if (get<ApplicationFeature>().isEnabled) {
                project.apply(BootstrapAppPlugin::class.java)
            } else if (get<LibraryFeature>().isEnabled) {
                project.apply(BootstrapLibPlugin::class.java)
            }
        }
    }

    override fun configure() {
        with(project.featureRegistry) {
            if (get<ApplicationFeature>().isEnabled) {
                val mainClass = project.propertiesRegistry.get<ApplicationProperties>().mainClass.get()
                project.tasks.withType<BootstrapAppTask> {
                    this.mainClass.set(mainClass)
                }
            }
        }
    }
}
