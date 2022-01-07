package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.Feature
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

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

        private val blueprints = ArrayList<Blueprint>()

        fun feature(supplier: (Project) -> Feature): Builder<P> {
            this.featureSupplier = supplier
            return this
        }

        fun properties(supplier: (Project) -> P): Builder<P> {
            this.propertiesSupplier = supplier
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
            blueprints.forEach { feature.addBlueprint(it) }
            return dsl
        }
    }
}
