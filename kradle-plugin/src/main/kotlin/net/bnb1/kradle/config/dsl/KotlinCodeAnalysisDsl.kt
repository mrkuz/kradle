package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class KotlinCodeAnalysisDsl(features: AllFeatures, properties: AllProperties) {

    val detekt = FeatureDsl(features.detekt, properties.detekt)
    val detektConfigFile = properties.detekt.configFile
    val detektVersion = properties.detekt.version
}
