package net.bnb1.kradle.dsl

import org.gradle.api.provider.Property

open class OptionalValue(
    private val property: Property<String>,
    private val suggestion: String
) : SimpleProvider<String> {

    override val notNull: Boolean
        get() = property.isPresent

    override fun get() = property.get()

    operator fun invoke(version: String = suggestion) = set(version)

    fun set(version: String = suggestion) = property.set(version)

    fun bind(property: Property<String>) = this.property.set(property)

    fun get(default: String) = property.getOrElse(default)

    fun reset() = property.set(null)

    fun unset() = property.set(null)

    val hasValue by ::notNull
}
