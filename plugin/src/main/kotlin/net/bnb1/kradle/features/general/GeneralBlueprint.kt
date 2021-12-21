package net.bnb1.kradle.features.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

class GeneralBlueprint(project : Project) : Blueprint(project) {

    override fun onActivate() {
        project.featureRegistry.map.values
            .filter { it.isEnabled() }
            .filter { it.isParent(GeneralFeature::class) }
            .forEach { it.activate() }
    }
}
