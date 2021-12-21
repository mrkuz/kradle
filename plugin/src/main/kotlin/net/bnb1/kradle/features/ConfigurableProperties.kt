package net.bnb1.kradle.features

interface ConfigurableProperties<P : Properties> {

    operator fun invoke(action: P.() -> Unit = {}) = configure(action)

    fun configure(action: P.() -> Unit = {})
}