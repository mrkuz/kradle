package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.empty
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class DockerProperties(project: Project) : Properties(project) {

    val baseImage = property(factory.property("bellsoft/liberica-openjdk-alpine:${Catalog.Versions.jvm}"))

    val ports = factory.setProperty(Int::class.java)

    val jvmKillVersion = property(factory.empty<String>())
    fun withJvmKill(version: String = Catalog.Versions.jvmKill) = jvmKillVersion.set(version)

    val withAppSh = property(factory.property(false))

    val javaOpts = property(factory.empty<String>())
    val jvmOpts = javaOpts
}
