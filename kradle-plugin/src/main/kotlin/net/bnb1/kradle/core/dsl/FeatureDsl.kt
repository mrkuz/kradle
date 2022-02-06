package net.bnb1.kradle.core.dsl

import net.bnb1.kradle.core.Feature

class FeatureDsl<T : Any>(private val feature: Feature, private val target: T) {

    operator fun invoke(enable: Boolean = true) {
        if (enable) {
            enable()
        } else {
            disable()
        }
    }

    operator fun invoke(action: T.() -> Unit) = enable(action)

    fun configureOnly(action: T.() -> Unit = {}) {
        action(target)
    }

    fun enable(action: T.() -> Unit = {}) {
        action(target)
        feature.enable()
    }

    fun disable() {
        feature.disable()
    }
}
