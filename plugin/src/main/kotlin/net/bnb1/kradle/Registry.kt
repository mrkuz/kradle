package net.bnb1.kradle

import kotlin.reflect.KClass

open class Registry<T : Any> {

    private val _map = mutableMapOf<KClass<*>, T>()
    val map: Map<KClass<*>, T>
        get() = _map

    fun register(instance: T): Boolean = _map.putIfAbsent(instance::class, instance) == null

    @Suppress("UNCHECKED_CAST")
    inline fun <reified U : T> get() = map[U::class] as U

    @Suppress("UNCHECKED_CAST")
    fun <U : T> get(key: KClass<U>) = map[key] as U
}
