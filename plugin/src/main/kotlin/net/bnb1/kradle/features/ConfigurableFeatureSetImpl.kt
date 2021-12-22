package net.bnb1.kradle.features

import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project

class ConfigurableFeatureSetImpl<P : Properties>(
    private val featureSet: FeatureSet,
    private val properties: P,
) : ConfigurableFeatureSet<P> {

    fun register(project: Project): ConfigurableFeatureSetImpl<P> {
        project.propertiesRegistry.register(properties)
        return this
    }

    fun asInterface() = this as ConfigurableFeatureSet<P>

    override fun configureOnly(action: P.() -> Unit) {
        action(properties)
    }

    override fun tryActivate(action: P.() -> Unit): Boolean {
        action(properties)
        return featureSet.tryActivate()
    }

    override fun activate(action: P.() -> Unit) {
        action(properties)
        featureSet.activate()
    }
}
