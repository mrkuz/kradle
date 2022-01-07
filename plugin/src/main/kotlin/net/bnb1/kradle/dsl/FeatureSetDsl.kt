package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.FeatureSet
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.support.Tracer

class FeatureSetDsl<P : Properties>(
    private val tracer: Tracer,
    private val featureSet: FeatureSet,
    private val properties: P
) {

    operator fun invoke(action: P.() -> Unit = {}) = activate(action)

    fun activate(action: P.() -> Unit = {}) {
        action(properties)
        featureSet.activate(tracer)
    }

    fun configureOnly(action: P.() -> Unit = {}) {
        action(properties)
    }

    fun tryActivate(action: P.() -> Unit = {}): Boolean {
        action(properties)
        return featureSet.tryActivate(tracer)
    }
}
