package net.bnb1.kradle.features

import org.gradle.api.provider.Property

open class PropertyDsl<T : Any>(val property: Property<T>, private val defaultValue: T?) {

    init {
        if (defaultValue != null) {
            property.convention(defaultValue)
        }
    }

    operator fun invoke(value: T) = set(value)

    fun set(value: T) = property.set(value)

    fun bind(property: Property<T>) = this.property.set(property)

    fun get() = property.get()

    fun get(default: T) = property.getOrElse(default)

    fun reset() {
        property.set(defaultValue)
    }

    fun unset() = property.set(null)

    val hasValue
        get() = property.isPresent
}
