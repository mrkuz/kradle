package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDsl
import org.gradle.api.Project

class JavaLintProperties(project: Project) : Properties(project) {

    val checkstyle = PropertiesDsl.Builder<CheckstyleProperties>(project)
        .properties { CheckstyleProperties(it) }
        .build()
}
