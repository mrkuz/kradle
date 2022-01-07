package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.FeatureSet
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project

class FeatureSetDsl<P : Properties> private constructor(
    private val featureSet: FeatureSet,
    private val properties: P
) {

    operator fun invoke(action: P.() -> Unit = {}) = activate(action)

    fun configureOnly(action: P.() -> Unit = {}) {
        action(properties)
    }

    fun tryActivate(action: P.() -> Unit = {}): Boolean {
        action(properties)
        return featureSet.tryActivate()
    }

    fun activate(action: P.() -> Unit = {}) {
        action(properties)
        featureSet.activate()
    }

    class Builder<P : Properties>(private val project: Project) {

        private var featureSetSupplier: ((Project) -> FeatureSet)? = null
        private var propertiesSupplier: ((Project) -> P)? = null

        fun featureSet(supplier: (Project) -> FeatureSet): Builder<P> {
            this.featureSetSupplier = supplier
            return this
        }

        fun properties(supplier: (Project) -> P): Builder<P> {
            this.propertiesSupplier = supplier
            return this
        }

        fun build(): FeatureSetDsl<P> {
            val featureSet = featureSetSupplier!!(project)
            val properties = propertiesSupplier!!(project).also { project.propertiesRegistry.register(it) }
            return FeatureSetDsl(featureSet, properties)
        }
    }
}
