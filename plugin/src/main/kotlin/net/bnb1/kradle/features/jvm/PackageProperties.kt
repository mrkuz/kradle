package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDslImpl
import org.gradle.api.Project

class PackageProperties(project: Project) : Properties(project) {

    val uberJar = PropertiesDslImpl(PackageUberJarProperties(project))
        .register(project)
        .asInterface()
}
