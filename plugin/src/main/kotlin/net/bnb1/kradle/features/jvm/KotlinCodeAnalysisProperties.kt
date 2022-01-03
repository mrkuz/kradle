package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDsl
import org.gradle.api.Project

class KotlinCodeAnalysisProperties(project: Project) : Properties(project) {

    private val detektProperties = DetektProperties(project)
    val detekt = PropertiesDsl.Builder<DetektProperties>(project)
        .properties(detektProperties)
        .build()
    val detektConfigFile = detektProperties.configFile
    val detektVersion = detektProperties.version
}
