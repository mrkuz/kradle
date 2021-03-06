package net.bnb1.kradle.core.dsl

import net.bnb1.kradle.core.FeatureSet
import net.bnb1.kradle.support.Tracer

class FeatureSetDsl<T : Any>(
    private val tracer: Tracer,
    private val featureSet: FeatureSet,
    private val target: T
) {

    operator fun invoke(action: T.() -> Unit = {}) = activate(action)

    fun configureOnly(action: T.() -> Unit = {}) {
        action(target)
    }

    fun activate(action: T.() -> Unit = {}) {
        action(target)
        featureSet.activate(tracer)
    }

    fun tryActivate(action: T.() -> Unit = {}): Boolean {
        action(target)
        return featureSet.tryActivate(tracer)
    }
}
