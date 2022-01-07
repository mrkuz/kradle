package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties

class KotlinCodeAnalysisProperties(context: KradleContext) : Properties() {

    private val _detektProperties by context { DetektProperties() }
    val detekt = PropertiesDsl(_detektProperties)
    val detektConfigFile = _detektProperties.configFile
    val detektVersion = _detektProperties.version
}
