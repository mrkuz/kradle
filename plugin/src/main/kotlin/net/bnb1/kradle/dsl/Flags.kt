package net.bnb1.kradle.dsl

import org.gradle.api.provider.SetProperty

open class Flags(private val property: SetProperty<String>, private val invert: Boolean) :
    SimpleProvider<Set<String>> {

    override val notNull: Boolean
        get() = true

    override fun get(): Set<String> = property.get()

    operator fun invoke(action: Flags.() -> Unit = {}) = action(this)

    fun bind(property: SetProperty<String>) = this.property.set(property)

    fun reset() = property.set(setOf())

    fun enable(value: String) = property.set(
        if (!invert) {
            property.get() + value
        } else {
            property.get() - value
        }
    )

    fun disable(value: String) = property.set(
        if (!invert) {
            property.get() - value
        } else {
            property.get() + value
        }
    )
}
