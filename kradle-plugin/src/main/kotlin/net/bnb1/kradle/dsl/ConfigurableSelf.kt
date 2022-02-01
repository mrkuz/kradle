package net.bnb1.kradle.dsl

interface ConfigurableSelf<T : ConfigurableSelf<T>> {

    operator fun invoke(action: T.() -> Unit = {}) = configure(action)

    @Suppress("UNCHECKED_CAST")
    fun configure(action: T.() -> Unit = {}) = action(this as T)
}
