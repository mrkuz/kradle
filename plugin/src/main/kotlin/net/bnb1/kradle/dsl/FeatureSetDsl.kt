package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.FeatureSet
import net.bnb1.kradle.features.Properties

class FeatureSetDsl<P : Properties>(private val featureSet: FeatureSet, private val properties: P) {

    operator fun invoke(action: P.() -> Unit = {}) = activate(action)

    fun activate(action: P.() -> Unit = {}) {
        action(properties)
        featureSet.activate()
    }

    fun configureOnly(action: P.() -> Unit = {}) {
        action(properties)
    }

    fun tryActivate(action: P.() -> Unit = {}): Boolean {
        action(properties)
        return featureSet.tryActivate()
    }
}
