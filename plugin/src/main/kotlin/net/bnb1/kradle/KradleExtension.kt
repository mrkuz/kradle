package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class KradleExtension @Inject constructor(factory: ObjectFactory) {

    private val _mainClass = factory.empty<String>()
    fun mainClass(name: String) = _mainClass.set(name)
    val mainClass
        get() = if (!_mainClass.isPresent) {
            ""
        } else if (_mainClass.get().endsWith("Kt")) {
            _mainClass.get()
        } else {
            _mainClass.get() + "Kt"
        }

    val targetJvm = factory.property("16")
    fun targetJvm(version: String) = targetJvm.set(version)

    val kotlinxCoroutinesVersion = factory.property("1.5.1")
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

    val jmhVersion = factory.property("1.21")
    fun jmhVersion(version: String) = jmhVersion.set(version)

    val ktlintVersion = factory.property("0.42.1")
    fun ktlintVersion(version: String) = ktlintVersion.set(version)

    val detektConfigFile = factory.property("detekt-config.yml")
    fun detektConfigFile(name: String) = detektConfigFile.set(name)
    val detektVersion = factory.property("1.18.1")
    fun detektVersion(version: String) = detektVersion.set(version)

    private val disabledBlueprints = factory.setProperty(Class::class.java)
    fun disable(blueprint: Class<out PluginBlueprint<Plugin<Project>>>) = disabledBlueprints.add(blueprint)
    fun isDisabled(blueprint: PluginBlueprint<Plugin<Project>>) =
        disabledBlueprints.get().contains(blueprint::class.java)

    open class UberJarExtension(factory: ObjectFactory) {

        val minimize = factory.property(false)
        fun minimize(enabled: Boolean) = minimize.set(enabled)
    }

    open class ImageExtension(factory: ObjectFactory) {

        val baseImage = factory.property("bellsoft/liberica-openjdk-alpine:16")
        fun baseImage(name: String) = baseImage.set(name)

        val ports = factory.setProperty(Int::class.java)

        val jvmKillVersion = factory.empty<String>()
        fun withJvmKill(version: String = "1.16.0") = jvmKillVersion.set(version)

        val withAppSh = factory.property(false)
        fun withAppSh() = withAppSh.set(true)

        val javaOpts = factory.empty<String>()
        fun javaOpts(opts: String) = javaOpts.set(opts)
    }

    open class TestsExtension(factory: ObjectFactory) {

        val junitJupiterVersion = factory.property("5.7.2")
        fun junitJupiterVersion(version: String) = junitJupiterVersion.set(version)

        val jacocoVersion = factory.property("0.8.7")
        fun jacocoVersion(version: String) = jacocoVersion.set(version)

        val kotestVersion = factory.empty<String>()
        fun useKotest(version: String = "4.6.1") = kotestVersion.set(version)

        val mockkVersion = factory.empty<String>()
        fun useMockk(version: String = "1.12.0") = mockkVersion.set(version)
    }
}