package net.bnb1.kradle.presets

import kotlin.reflect.KClass

class PresetRegistry {

    private val _map = mutableMapOf<KClass<*>, Preset>()
    val map: Map<KClass<*>, Preset>
        get() = _map

    fun register(feature: Preset): Boolean = _map.putIfAbsent(feature::class, feature) == null

    @Suppress("UNCHECKED_CAST")
    inline fun <reified P : Preset> get() = map[P::class] as P

    @Suppress("UNCHECKED_CAST")
    fun <P : Preset> get(key: KClass<P>) = map[key] as P
}
