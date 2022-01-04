package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class DockerProperties(project: Project) : Properties(project) {

    val baseImage = property("bellsoft/liberica-openjdk-alpine:${Catalog.Versions.jvm}")

    val ports = factory.setProperty(Int::class.java)
    fun ports(vararg elements: Int) = this.ports.addAll(elements.toSet())

    val jvmKillVersion = property<String>()
    fun withJvmKill(version: String = Catalog.Versions.jvmKill) = jvmKillVersion.set(version)

    val withAppSh = property(false)
    fun appSh(enabled: Boolean = true) = withAppSh(enabled)

    val javaOpts = property<String>()
    val jvmOpts = javaOpts
}
