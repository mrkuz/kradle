package net.bnb1.kradle.features

interface FeatureSetDsl<P : Properties> {

    operator fun invoke(action: P.() -> Unit = {}) = activate(action)

    fun configureOnly(action: P.() -> Unit = {})

    fun tryActivate(action: P.() -> Unit = {}): Boolean
    
    fun activate(action: P.() -> Unit = {})
}
