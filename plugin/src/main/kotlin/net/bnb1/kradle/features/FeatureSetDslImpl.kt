package net.bnb1.kradle.features

import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project

class FeatureSetDslImpl<P : Properties>(
    private val featureSet: FeatureSet,
    private val properties: P
) : FeatureSetDsl<P> {

    fun register(project: Project): FeatureSetDslImpl<P> {
        project.propertiesRegistry.register(properties)
        return this
    }

    fun asInterface() = this as FeatureSetDsl<P>

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
