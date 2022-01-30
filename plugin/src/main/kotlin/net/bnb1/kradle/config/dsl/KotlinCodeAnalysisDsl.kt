package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class KotlinCodeAnalysisDsl(properties: AllProperties) {

    val detekt = Configurable(properties.detekt)
    val detektConfigFile = properties.detekt.configFile
    val detektVersion = properties.detekt.version
}
