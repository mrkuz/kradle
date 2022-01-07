package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class KotlinCodeAnalysisProperties(project: Project) : Properties() {

    private val detektProperties = DetektProperties()
    val detekt = PropertiesDsl.Builder<DetektProperties>(project)
        .properties(detektProperties)
        .build()
    val detektConfigFile = detektProperties.configFile
    val detektVersion = detektProperties.version
}
