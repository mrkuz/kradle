package net.bnb1.kradle.dsl

open class Value<T : Any>(private val defaultValue: T?) : SimpleProvider<T> {

    private var value: T? = defaultValue

    override val notNull: Boolean
        get() = value != null

    override fun get() = value!!

    operator fun invoke(value: T?) = set(value)

    fun set(value: T?) {
        this.value = value
    }

    fun get(default: T) = value ?: default

    fun reset() {
        value = defaultValue
    }

    fun unset() {
        value = null
    }

    val hasValue by ::notNull
}
