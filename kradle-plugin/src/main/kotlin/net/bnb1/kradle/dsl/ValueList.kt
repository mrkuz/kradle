package net.bnb1.kradle.dsl

open class ValueList<T : Any> : SimpleProvider<List<T>> {

    private val values = mutableListOf<T>()

    override val notNull: Boolean
        get() = true

    override fun get(): List<T> = values.toList()

    operator fun invoke(vararg values: T) = set(*values)

    fun set(vararg values: T) {
        this.values.clear()
        this.values.addAll(values)
    }

    fun add(value: T) {
        values.add(value)
    }

    fun remove(value: T) {
        values.remove(value)
    }

    fun reset() {
        values.clear()
    }
}
