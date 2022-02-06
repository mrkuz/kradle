package net.bnb1.kradle.dsl

@Suppress("MemberNameEqualsClassName")
open class Value<T : Any>(
    private val defaultValue: T?,
    private val suggestion: T?
) : SimpleProvider<T> {

    private var value: T? = defaultValue

    override val notNull: Boolean
        get() = value != null

    override fun get() = value!!

    operator fun invoke(value: T? = suggestion) = set(value)

    fun set(value: T? = suggestion) {
        this.value = value
    }

    fun get(default: T) = value ?: default

    fun get(consumer: (T) -> Unit) {
        if (hasValue) {
            consumer(value!!)
        }
    }

    fun reset() {
        value = defaultValue
    }

    fun unset() {
        value = null
    }

    val hasValue by ::notNull
    val hasSuggestion = suggestion != null
}
