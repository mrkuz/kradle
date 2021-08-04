package net.bnb1.kradle

import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class KradleExtension @Inject constructor(factory: ObjectFactory) {

    val targetJvm = factory.property("16")
    val kotlinxCoroutinesVersion = factory.property("1.5.1")

    val junitJupiterVersion = factory.property("5.7.2")

    val image = ImageExtension(factory)
    fun image(configure: ImageExtension.() -> Unit) = configure(image)

    val kotestVersion = factory.empty<String>()
    fun useKotest(version: String = "4.6.1") {
        kotestVersion.set(version)
    }

    open class ImageExtension(factory: ObjectFactory) {

        val baseImage = factory.property("bellsoft/liberica-openjdk-alpine-musl:16")
        val ports = factory.setProperty(Int::class.java)
    }
}