package net.bnb1.kradle.dsl

open class Flag(protected val target: (Boolean) -> Unit) : SimpleProvider<Boolean> {

    private var value: Boolean = false

    override val notNull: Boolean
        get() = true

    override fun get() = value

    operator fun invoke(enabled: Boolean = true) = set(enabled)

    fun set(value: Boolean) {
        this.value = value
        target(value)
    }

    fun enable() = set(true)

    fun disable() = set(false)

    fun toggle() = set(!value)
}
