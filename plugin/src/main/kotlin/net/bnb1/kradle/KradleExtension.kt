package net.bnb1.kradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class KradleExtension @Inject constructor(factory: ObjectFactory) {

    val junitJupiterVersion: Property<String> = factory.property(String::class.java)

    init {
        junitJupiterVersion.convention("5.7.2")
    }
}