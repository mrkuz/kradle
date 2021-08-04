package net.bnb1.kradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

inline fun <reified T> ObjectFactory.property(default: T): Property<T> {
    return property(T::class.java).apply {
        convention(default)
    }
}

inline fun <reified T> ObjectFactory.empty(): Property<T> {
    return property(T::class.java)
}