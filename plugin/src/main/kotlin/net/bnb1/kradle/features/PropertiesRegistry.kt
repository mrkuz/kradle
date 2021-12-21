package net.bnb1.kradle.features

import kotlin.reflect.KClass

class PropertiesRegistry {

    private val _map = mutableMapOf<KClass<*>, Properties>()
    val map : Map<KClass<*>, Properties>
        get() = _map

    fun register(properties: Properties): Boolean = _map.putIfAbsent(properties::class, properties) == null

    @Suppress("UNCHECKED_CAST")
    inline fun <reified P : Properties> get() = map[P::class] as P

    @Suppress("UNCHECKED_CAST")
    fun <P : Properties> get(key: KClass<P>) = map[key] as P
}
