package net.bnb1.kradle.features

interface PropertyWrapper<T : Any> {

    val notNull: Boolean

    fun get(): T
}
