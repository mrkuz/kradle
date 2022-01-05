package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class CodeAnalysisProperties(project: Project) : Properties(project) {

    val ignoreFailures = property(false)
}
