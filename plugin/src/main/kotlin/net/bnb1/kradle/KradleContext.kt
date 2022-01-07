package net.bnb1.kradle

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

open class KradleContext {

    private val _map = mutableMapOf<KClass<*>, Any>()
    val map: Map<KClass<*>, Any>
        get() = _map

    fun register(instance: Any): Boolean = _map.putIfAbsent(instance::class, instance) == null

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> get() = map[T::class] as T

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(key: KClass<T>) = _map[key] as T

    fun <T : Any> getSubclassOf(key: KClass<T>) =
        _map.entries.asSequence()
            .filter { it.key.isSubclassOf(key) }
            .map { it.value as T }
            .toList()
}
