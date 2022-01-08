package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.FeatureSet
import net.bnb1.kradle.support.Tracer

class FeatureSetDsl<C : Any>(
    private val tracer: Tracer,
    private val featureSet: FeatureSet,
    private val configuration: C
) {

    operator fun invoke(action: C.() -> Unit = {}) = activate(action)

    fun activate(action: C.() -> Unit = {}) {
        action(configuration)
        featureSet.activate(tracer)
    }

    fun configureOnly(action: C.() -> Unit = {}) {
        action(configuration)
    }

    fun tryActivate(action: C.() -> Unit = {}): Boolean {
        action(configuration)
        return featureSet.tryActivate(tracer)
    }
}
