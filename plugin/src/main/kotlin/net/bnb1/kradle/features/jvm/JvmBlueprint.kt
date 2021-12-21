package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

class JvmBlueprint(project : Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(JavaPlugin::class.java)
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<JvmProperties>()
        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        val javaVersion = JavaVersion.toVersion(properties.targetJvm.get())
        javaExtension.sourceCompatibility = javaVersion
        javaExtension.targetCompatibility = javaVersion
    }

    override fun onActivate() {
        project.featureRegistry.map.values
            .filter { it.isEnabled() }
            .filter { it.isParent(JvmFeature::class) }
            .forEach { it.activate() }
    }
}
