package net.bnb1.kradle.features

import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project
import kotlin.reflect.KClass

class FeatureDslImpl<P : Properties>(
    private val feature: Feature,
    private val properties: P
) : FeatureDsl<P> {

    fun register(project: Project): FeatureDslImpl<P> {
        project.featureRegistry.register(feature)
        project.propertiesRegistry.register(properties)
        return this
    }

    fun setParent(parent: KClass<out FeatureSet>): FeatureDslImpl<P> {
        feature.setParent(parent)
        return this
    }

    fun after(vararg features: KClass<out Feature>): FeatureDslImpl<P> {
        feature.after(*features)
        return this
    }

    fun addBlueprint(blueprint: Blueprint): FeatureDslImpl<P> {
        feature.addBlueprint(blueprint)
        return this
    }

    fun asInterface() = this as FeatureDsl<P>

    override fun enable(action: P.() -> Unit) {
        action(properties)
        feature.enable()
    }

    override fun disable() {
        feature.disable()
    }
}
