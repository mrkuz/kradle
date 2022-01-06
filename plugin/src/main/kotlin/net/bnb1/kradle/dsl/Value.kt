package net.bnb1.kradle.dsl

import org.gradle.api.provider.Property

open class Value<T : Any>(val property: Property<T>, private val defaultValue: T?) : PropertyWrapper<T> {

    init {
        if (defaultValue != null) {
            property.convention(defaultValue)
        }
    }

    override val notNull: Boolean
        get() = property.isPresent

    override fun get() = property.get()

    operator fun invoke(value: T) = set(value)

    fun set(value: T) = property.set(value)

    fun bind(property: Property<T>) = this.property.set(property)

    fun get(default: T) = property.getOrElse(default)

    fun reset() = property.set(defaultValue)

    fun unset() = property.set(null)

    val hasValue by ::notNull
}
