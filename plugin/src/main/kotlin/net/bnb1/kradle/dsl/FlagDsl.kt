package net.bnb1.kradle.dsl

import org.gradle.api.provider.Property

open class FlagDsl(private val property: Property<Boolean>) : PropertyWrapper<Boolean> {

    init {
        property.convention(false)
    }

    override val notNull: Boolean
        get() = true

    override fun get() = property.get()

    operator fun invoke(enabled: Boolean = true) = set(enabled)

    fun set(value: Boolean) = property.set(value)

    fun bind(property: Property<Boolean>) = this.property.set(property)

    fun enable() = set(true)

    fun disable() = set(false)
}
