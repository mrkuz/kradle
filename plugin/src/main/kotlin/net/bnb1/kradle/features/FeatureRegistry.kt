package net.bnb1.kradle.features

import kotlin.reflect.KClass

class FeatureRegistry {

    private val _map = mutableMapOf<KClass<*>, Feature>()
    val map : Map<KClass<*>, Feature>
        get() = _map

    fun register(feature: Feature): Boolean = _map.putIfAbsent(feature::class, feature) == null

    @Suppress("UNCHECKED_CAST")
    inline fun <reified F : Feature> get() = map[F::class] as F

    @Suppress("UNCHECKED_CAST")
    fun <F : Feature> get(key: KClass<F>) = map[key] as F
}
