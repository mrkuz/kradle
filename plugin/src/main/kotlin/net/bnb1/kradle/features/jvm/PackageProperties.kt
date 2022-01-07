package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class PackageProperties(project: Project) : Properties() {

    val uberJar = PropertiesDsl.Builder<PackageUberJarProperties>(project)
        .properties { PackageUberJarProperties() }
        .build()
}
