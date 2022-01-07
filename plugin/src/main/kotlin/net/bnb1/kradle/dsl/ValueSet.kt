package net.bnb1.kradle.dsl

open class ValueSet<T : Any> : SimpleProvider<Set<T>> {

    private val values = mutableSetOf<T>()

    override val notNull: Boolean
        get() = true

    override fun get(): Set<T> = values.toSet()

    operator fun invoke(vararg values: T) = set(*values)

    fun set(vararg values: T) {
        this.values.clear()
        this.values.addAll(values)
    }

    fun reset() {
        values.clear()
    }

    fun add(value: T) {
        values.add(value)
    }

    fun remove(value: T) {
        values.remove(value)
    }
}
