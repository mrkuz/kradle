package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class ApplicationProperties(project: Project) : Properties(project) {

    val mainClass = value<String>()
}
