package net.bnb1.kradle.dsl

open class OptionalValue<T : Any>(
    private val suggestion: T
) : SimpleProvider<T> {

    private var value: T? = null

    override val notNull: Boolean
        get() = value != null

    override fun get() = value!!

    operator fun invoke(value: T? = suggestion) = set(value)

    fun set(value: T? = suggestion) {
        this.value = value
    }

    fun get(default: T) = value ?: default

    fun reset() {
        value = null
    }

    fun unset() {
        value = null
    }

    val hasValue by ::notNull
}
