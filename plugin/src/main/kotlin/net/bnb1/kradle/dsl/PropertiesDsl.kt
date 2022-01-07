package net.bnb1.kradle.dsl

import net.bnb1.kradle.features.Properties

class PropertiesDsl<P : Properties>(private val properties: P) {

    operator fun invoke(action: P.() -> Unit = {}) = configure(action)

    fun configure(action: P.() -> Unit = {}) = action(properties)
}
