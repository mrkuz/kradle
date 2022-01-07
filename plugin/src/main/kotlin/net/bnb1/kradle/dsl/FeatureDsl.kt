package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.Feature
import net.bnb1.kradle.features.Properties

class FeatureDsl<P : Properties>(private val feature: Feature, private val properties: P) {

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
}
