package net.bnb1.kradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class KradleExtension @Inject constructor(factory: ObjectFactory) {

    val targetJvm = factory.property("1.8")
    val kotlinxCoroutinesVersion = factory.property("1.5.1")

    val junitJupiterVersion = factory.property("5.7.2")
    val kotestVersion = factory.empty<String>()

    fun useKotest(version: String = "4.6.1") {
        kotestVersion.set(version)
    }

    private inline fun <reified T> ObjectFactory.property(default: T): Property<T> {
        return property(T::class.java).apply {
            convention(default)
        }
    }

    private inline fun <reified T> ObjectFactory.empty(): Property<T> {
        return property(T::class.java)
    }
}