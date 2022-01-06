package net.bnb1.kradle.dsl

import org.gradle.api.provider.SetProperty

open class ValueSet<T : Any>(private val property: SetProperty<T>) : PropertyWrapper<Set<T>> {

    override val notNull: Boolean
        get() = true

    override fun get(): Set<T> = property.get()

    operator fun invoke(vararg values: T) = property.set(values.toSet())

    fun set(vararg values: T) = property.set(values.toSet())

    fun bind(property: SetProperty<T>) = this.property.set(property)

    fun reset() = property.set(setOf())

    fun add(value: T) = property.set(property.get() + value)

    fun remove(value: T) = property.set(property.get() - value)
}
