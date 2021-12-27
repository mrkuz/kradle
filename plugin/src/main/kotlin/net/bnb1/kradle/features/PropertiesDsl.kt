package net.bnb1.kradle.features

interface PropertiesDsl<P : Properties> {

    operator fun invoke(action: P.() -> Unit = {}) = configure(action)
    
    fun configure(action: P.() -> Unit = {})
}
