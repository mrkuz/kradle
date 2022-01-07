package net.bnb1.kradle

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
open class KradleContext {

    private val _map = mutableMapOf<KClass<*>, Any>()
    val map: Map<KClass<*>, Any>
        get() = _map

    fun register(instance: Any): Boolean = _map.putIfAbsent(instance::class, instance) == null

    inline fun <reified T : Any> get() = map[T::class] as T

    operator fun <T : Any> get(key: KClass<T>) = _map[key] as T

    fun <T : Any> getSubclassOf(key: KClass<T>) =
        _map.entries.asSequence()
            .filter { it.key.isSubclassOf(key) }
            .map { it.value as T }
            .toList()

    operator fun <T : Any> invoke(initialize: () -> T) = Delegate(this, initialize)

    class Delegate<T : Any>(private val context: KradleContext, initialize: () -> T) {

        init {
            val value = initialize()
            context.register(value)
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return context[property.returnType.jvmErasure as KClass<T>]
        }
    }
}
