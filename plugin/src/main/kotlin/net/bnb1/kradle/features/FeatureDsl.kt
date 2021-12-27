package net.bnb1.kradle.features

interface FeatureDsl<P : Properties> {

    operator fun invoke(action: P.() -> Unit = {}) = enable(action)

    fun enable(action: P.() -> Unit = {})

    fun disable()
}
