package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.ConfigurablePropertiesImpl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class PackageProperties(project: Project) : Properties(project) {

    val uberJar = ConfigurablePropertiesImpl(PackageUberJarProperties(project))
        .register(project)
        .asInterface()
}