package net.bnb1.kradle.dsl

import org.gradle.api.provider.Property

open class OptionalVersion(
    private val property: Property<String>,
    private val suggestedVersion: String
) : PropertyWrapper<String> {

    override val notNull: Boolean
        get() = property.isPresent

    override fun get() = property.get()

    operator fun invoke(version: String = suggestedVersion) = set(version)

    fun set(version: String = suggestedVersion) = property.set(version)

    fun bind(property: Property<String>) = this.property.set(property)

    fun get(default: String) = property.getOrElse(default)

    fun reset() = property.set(null)

    fun unset() = property.set(null)

    val hasValue by ::notNull
}
