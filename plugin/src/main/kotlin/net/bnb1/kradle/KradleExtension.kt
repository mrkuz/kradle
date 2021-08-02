package net.bnb1.kradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class KradleExtension @Inject constructor(factory: ObjectFactory) {

    val junitJupiterVersion: Property<String> = factory.property("5.7.2")
    val kotlinxCoroutinesVersion: Property<String> = factory.property("1.5.1")

    private inline fun <reified T> ObjectFactory.property(default: T): Property<T> {
        return property(T::class.java).apply {
            convention(default)
        }
    }
}