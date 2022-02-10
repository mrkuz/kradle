package net.bnb1.kradle.dsl

@Suppress("MemberNameEqualsClassName")
open class Value<T : Any>(
    private val defaultValue: T,
    private val target: (T) -> Unit
) : SimpleProvider<T> {

    private var value: T = defaultValue

    init {
        target(defaultValue)
    }

    override val notNull: Boolean
        get() = true

    override fun get() = value

    operator fun invoke(value: T) = set(value)

    fun set(value: T) {
        this.value = value
        target(value)
    }

    fun reset() {
        set(defaultValue)
    }
}
