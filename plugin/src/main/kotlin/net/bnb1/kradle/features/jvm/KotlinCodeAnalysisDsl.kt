package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.features.AllProperties

class KotlinCodeAnalysisDsl(properties: AllProperties) {

    val detekt = Configurable(properties.detekt)
    val detektConfigFile = properties.detekt.configFile
    val detektVersion = properties.detekt.version
}
