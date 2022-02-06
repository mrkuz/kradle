package net.bnb1.kradle.dsl

interface SimpleProvider<T : Any> {

    val notNull: Boolean

    fun get(): T
}
