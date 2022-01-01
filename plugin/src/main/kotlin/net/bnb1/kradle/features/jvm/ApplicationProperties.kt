package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.empty
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class ApplicationProperties(project: Project) : Properties(project) {

    val mainClass = property(factory.empty<String>())
}