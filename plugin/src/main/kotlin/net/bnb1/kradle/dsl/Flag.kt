package net.bnb1.kradle.dsl

open class Flag : SimpleProvider<Boolean> {

    private var value: Boolean = false

    override val notNull: Boolean
        get() = true

    override fun get() = value

    operator fun invoke(enabled: Boolean = true) = set(enabled)

    fun set(value: Boolean) {
        this.value = value
    }

    fun enable() = set(true)

    fun disable() = set(false)
}
