package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class PackageUberJarProperties(project: Project) : Properties(project) {

    val minimize = flag()
}
