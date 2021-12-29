package net.bnb1.kradle.v1

import Catalog
import net.bnb1.kradle.empty
import net.bnb1.kradle.property
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class KradleCompatExtension @Inject constructor(private val factory: ObjectFactory) {

    private val _mainClass = factory.empty<String>()
    fun mainClass(name: String, jvmName: Boolean = false) {
        if (jvmName) {
            _mainClass.set(name)
        } else {
            _mainClass.set(name + "Kt")
        }
    }

    val mainClass
        get() = _mainClass.getOrElse("")

    val targetJvm = factory.property(Catalog.Versions.jvm)
    fun targetJvm(version: String) = targetJvm.set(version)

    val kotlinxCoroutinesVersion = factory.property(Catalog.Versions.kotlinCoroutines)
    fun kotlinxCoroutinesVersion(version: String) = kotlinxCoroutinesVersion.set(version)

    val tests = TestsExtension(factory)
    fun tests(configure: TestsExtension.() -> Unit) = configure(tests)

    val uberJar = UberJarExtension(factory)
    fun uberJar(configure: UberJarExtension.() -> Unit) = configure(uberJar)

    val image = ImageExtension(factory)
    fun image(configure: ImageExtension.() -> Unit) = configure(image)

    // Backward compatibility
    val jacocoVersion = tests.jacocoVersion
    fun jacocoVersion(version: String) = tests.jacocoVersion.set(version)

    val jmhVersion = factory.property(Catalog.Versions.jmh)
    fun jmhVersion(version: String) = jmhVersion.set(version)

    val ktlintVersion = factory.property(Catalog.Versions.ktlint)
    fun ktlintVersion(version: String) = ktlintVersion.set(version)

    val detektConfigFile = factory.property("detekt-config.yml")
    fun detektConfigFile(name: String) = detektConfigFile.set(name)
    val detektVersion = factory.property(Catalog.Versions.detekt)
    fun detektVersion(version: String) = detektVersion.set(version)

    open class UberJarExtension(factory: ObjectFactory) {

        val minimize = factory.property(false)
        fun minimize(enabled: Boolean) = minimize.set(enabled)
    }

    open class ImageExtension(factory: ObjectFactory) {

        val baseImage = factory.property("bellsoft/liberica-openjdk-alpine:${Catalog.Versions.jvm}")
        fun baseImage(name: String) = baseImage.set(name)

        val ports = factory.setProperty(Int::class.java)

        val jvmKillVersion = factory.empty<String>()
        fun withJvmKill(version: String = Catalog.Versions.jvmKill) = jvmKillVersion.set(version)

        val withAppSh = factory.property(false)
        fun withAppSh() = withAppSh.set(true)

        val javaOpts = factory.empty<String>()
        fun javaOpts(opts: String) = javaOpts.set(opts)
    }

    open class TestsExtension(factory: ObjectFactory) {

        val junitJupiterVersion = factory.property(Catalog.Versions.junit)
        fun junitJupiterVersion(version: String) = junitJupiterVersion.set(version)

        val jacocoVersion = factory.property(Catalog.Versions.jacoco)
        fun jacocoVersion(version: String) = jacocoVersion.set(version)

        val kotestVersion = factory.empty<String>()
        fun useKotest(version: String = Catalog.Versions.kotest) = kotestVersion.set(version)

        val mockkVersion = factory.empty<String>()
        fun useMockk(version: String = Catalog.Versions.mockk) = mockkVersion.set(version)
    }
}
