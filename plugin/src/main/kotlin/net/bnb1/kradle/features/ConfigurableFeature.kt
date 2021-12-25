package net.bnb1.kradle.features

interface ConfigurableFeature<P : Properties> {

    operator fun invoke(action: P.() -> Unit = {}) = enable(action)

    fun enable(action: P.() -> Unit = {})

    fun disable()
}
