package net.bnb1.kradle.dsl

open class InvertedFlagSet(private val target: MutableSet<String>) : SimpleProvider<Set<String>> {

    override val notNull: Boolean
        get() = true

    override fun get(): Set<String> = target.toSet()

    operator fun invoke(action: InvertedFlagSet.() -> Unit = {}) = action(this)

    fun enable(value: String) {
        target.remove(value)
    }

    fun disable(value: String) {
        target.add(value)
    }

    fun reset() = target.clear()
}
