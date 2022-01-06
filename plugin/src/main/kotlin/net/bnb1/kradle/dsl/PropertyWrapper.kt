package net.bnb1.kradle.dsl

interface PropertyWrapper<T : Any> {

    val notNull: Boolean

    fun get(): T
}
