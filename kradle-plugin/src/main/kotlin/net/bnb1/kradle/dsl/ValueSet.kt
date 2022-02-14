package net.bnb1.kradle.dsl

open class ValueSet<T : Any>(private val target: MutableSet<T>) : SimpleProvider<Set<T>> {

    override val notNull: Boolean
        get() = true

    override fun get(): Set<T> = target.toSet()

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
