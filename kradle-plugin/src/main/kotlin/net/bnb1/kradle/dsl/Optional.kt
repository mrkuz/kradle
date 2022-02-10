package net.bnb1.kradle.dsl

@Suppress("MemberNameEqualsClassName")
open class Optional<T : Any>(
    private val suggestion: T?,
    private val target: (T?) -> Unit
) : SimpleProvider<T> {

    private var value: T? = null

    override val notNull: Boolean
        get() = value != null

    override fun get() = value!!

    operator fun invoke(value: T? = suggestion) = set(value)

    fun set(value: T? = suggestion) {
        this.value = value
        target(value)
    }

    fun get(default: T) = value ?: default

    fun unset() {
        set(null)
    }
}
