package net.bnb1.kradle.support

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Suppress("UNCHECKED_CAST")
open class Registry {

    private val _map = mutableMapOf<Pair<KClass<*>, String>, Any>()
    val map: Map<Pair<KClass<*>, String>, Any>
        get() = _map.toMap()

    fun add(instance: Any) = add(instance::class.qualifiedName!!, instance)

    fun add(name: String, instance: Any) {
        if (_map.putIfAbsent(Pair(instance::class, name), instance) != null) {
            throw IllegalArgumentException("Duplicate key: $name, ${instance::class.qualifiedName}")
        }
    }

    operator fun <T : Any> invoke(provider: () -> T) = create(provider)

    fun <T : Any> create(provider: () -> T): T {
        val instance = provider.invoke()
        add(instance)
        return instance
    }

    operator fun <T : Any> invoke(name: String, provider: (String) -> T) = create(name, provider)

    fun <T : Any> create(name: String, provider: (String) -> T): T {
        val instance = provider.invoke(name)
        add(name, instance)
        return instance
    }

    inline fun <reified T : Any> named(name: String) = map[Pair(T::class, name)] as T

    fun <T : Any> named(name: String, type: KClass<T>) = map[Pair(type, name)] as T

    fun <T : Any> withType() =
        _map.entries.asSequence()
            .filter { it.key.first.isSubclassOf(it.key.first) }
            .map { it.value as T }
            .toList()
}
