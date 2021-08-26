package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class KradleExtension @Inject constructor(factory: ObjectFactory) {

    val targetJvm = factory.property("16")
    val kotlinxCoroutinesVersion = factory.property("1.5.1")

    val tests = TestsExtension(factory)
    fun tests(configure: TestsExtension.() -> Unit) = configure(tests)

    val image = ImageExtension(factory)
    fun image(configure: ImageExtension.() -> Unit) = configure(image)

    val jacocoVersion = factory.property("0.8.7")
    val ktlintVersion = factory.property("0.42.1")

    private val disabledBlueprints = factory.setProperty(Class::class.java)
    fun disable(blueprint: Class<out PluginBlueprint<Plugin<Project>>>) = disabledBlueprints.add(blueprint)
    fun isDisabled(blueprint: PluginBlueprint<Plugin<Project>>) =
        disabledBlueprints.get().contains(blueprint::class.java)

    open class ImageExtension(factory: ObjectFactory) {

        val baseImage = factory.property("bellsoft/liberica-openjdk-alpine:16")
        val ports = factory.setProperty(Int::class.java)

        val jvmKillVersion = factory.empty<String>()
        fun useJvmKill(version: String = "1.16.0") = jvmKillVersion.set(version)

        val useAppSh = factory.property(false)
        fun useAppSh() = useAppSh.set(true)

        val javaOpts = factory.empty<String>()
    }

    open class TestsExtension(factory: ObjectFactory) {

        val junitJupiterVersion = factory.property("5.7.2")

        val kotestVersion = factory.empty<String>()
        fun useKotest(version: String = "4.6.1") = kotestVersion.set(version)

        val mockkVersion = factory.empty<String>()
        fun useMockk(version: String = "1.12.0") = mockkVersion.set(version)
    }
}