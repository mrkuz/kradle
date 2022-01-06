package net.bnb1.kradle.dsl

import org.gradle.api.provider.Property

open class Version(
    private val property: Property<String>,
    private val defaultVersion: String
) : PropertyWrapper<String> {

    init {
        property.convention(defaultVersion)
    }

    override val notNull: Boolean
        get() = property.isPresent

    override fun get() = property.get()

    operator fun invoke(version: String = defaultVersion) = set(version)

    fun set(version: String = defaultVersion) = property.set(version)

    fun bind(property: Property<String>) = this.property.set(property)

    fun get(default: String) = property.getOrElse(default)

    fun reset() = property.set(defaultVersion)

    fun unset() = property.set(null)

    val hasValue by ::notNull
}
