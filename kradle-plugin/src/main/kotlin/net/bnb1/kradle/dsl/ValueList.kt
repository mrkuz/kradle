package net.bnb1.kradle.dsl

open class ValueList<T : Any>(private val target: MutableList<T>) : SimpleProvider<List<T>> {

    override val notNull: Boolean
        get() = true

    override fun get(): List<T> = target.toList()

    operator fun invoke(vararg values: T) = set(*values)

    fun set(vararg values: T) {
        this.target.clear()
        this.target.addAll(values)
    }

    fun add(value: T) {
        target.add(value)
    }

    fun remove(value: T) {
        target.remove(value)
    }

    fun reset() {
        target.clear()
    }
}
