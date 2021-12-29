package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDsl
import org.gradle.api.Project

class PackageProperties(project: Project) : Properties(project) {

    val uberJar = PropertiesDsl.Builder<PackageUberJarProperties>(project)
        .properties { PackageUberJarProperties(it) }
        .build()
}
