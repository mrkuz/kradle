package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.Feature

class FeatureDsl<C : Any>(private val feature: Feature, private val configuration: C) {

    operator fun invoke(enable: Boolean = true) {
        if (enable) {
            enable()
        } else {
            disable()
        }
    }

    operator fun invoke(action: C.() -> Unit) = enable(action)

    fun enable(action: C.() -> Unit = {}) {
        action(configuration)
        feature.enable()
    }

    fun disable() {
        feature.disable()
    }
}
