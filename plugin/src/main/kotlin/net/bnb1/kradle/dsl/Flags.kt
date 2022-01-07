package net.bnb1.kradle.dsl

open class Flags(private val invert: Boolean) :
    SimpleProvider<Set<String>> {

    private val values = mutableSetOf<String>()

    override val notNull: Boolean
        get() = true

    override fun get(): Set<String> = values.toSet()

    operator fun invoke(action: Flags.() -> Unit = {}) = action(this)

    fun enable(value: String) {
        if (!invert) {
            values.add(value)
        } else {
            values.remove(value)
        }
    }

    fun disable(value: String) {
        if (!invert) {
            values.remove(value)
        } else {
            values.add(value)
        }
    }

    fun reset() = values.clear()
}
