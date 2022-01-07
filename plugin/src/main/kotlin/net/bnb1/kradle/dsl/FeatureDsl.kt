package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.Feature
import net.bnb1.kradle.features.FeatureSet
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project
import kotlin.reflect.KClass

class FeatureDsl<P : Properties> private constructor(
    private val feature: Feature,
    private val properties: P
) {

    operator fun invoke(enable: Boolean = true) {
        if (enable) {
            enable()
        } else {
            disable()
        }
    }

    operator fun invoke(action: P.() -> Unit) = enable(action)

    fun enable(action: P.() -> Unit = {}) {
        action(properties)
        feature.enable()
    }

    fun disable() {
        feature.disable()
    }

    class Builder<P : Properties>(private val project: Project) {

        private var featureSupplier: ((Project) -> Feature)? = null
        private var propertiesSupplier: ((Project) -> P)? = null

        private var parent: KClass<out FeatureSet>? = null
        private val after = ArrayList<KClass<out Feature>>()
        private val blueprints = ArrayList<Blueprint>()

        fun feature(supplier: (Project) -> Feature): Builder<P> {
            this.featureSupplier = supplier
            return this
        }

        fun properties(supplier: (Project) -> P): Builder<P> {
            this.propertiesSupplier = supplier
            return this
        }

        fun parent(parent: KClass<out FeatureSet>): Builder<P> {
            this.parent = parent
            return this
        }

        fun after(vararg features: KClass<out Feature>): Builder<P> {
            after.addAll(features)
            return this
        }

        fun addBlueprint(blueprint: Blueprint): Builder<P> {
            blueprints.add(blueprint)
            return this
        }

        fun build(): FeatureDsl<P> {
            val feature = featureSupplier!!(project)
            val properties = propertiesSupplier!!(project)

            val dsl = FeatureDsl(feature, properties)
            if (parent != null) {
                feature.setParent(parent!!)
            }
            after.forEach { feature.after(it) }
            blueprints.forEach { feature.addBlueprint(it) }
            return dsl
        }
    }
}
