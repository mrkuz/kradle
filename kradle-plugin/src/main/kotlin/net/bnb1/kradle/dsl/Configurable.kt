package net.bnb1.kradle.dsl

class Configurable<T : Any>(private val target: T) {

    operator fun invoke(action: T.() -> Unit = {}) = configure(action)

    fun configure(action: T.() -> Unit = {}) = action(target)
}
