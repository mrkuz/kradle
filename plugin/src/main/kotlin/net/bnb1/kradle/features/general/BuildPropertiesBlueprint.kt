package net.bnb1.kradle.features.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.Feature
import net.bnb1.kradle.plugins.BuildPropertiesPlugin
import org.gradle.api.Project
import kotlin.reflect.KClass

class BuildPropertiesBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(BuildPropertiesPlugin::class.java)
    }
    
    // KotlinFeature, JavaFeature
    override fun onFeatureActivate(feature: KClass<out Feature>) {
        project.tasks.named("processResources").configure {
            finalizedBy("generateBuildProperties")
        }
    }
}
