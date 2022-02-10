package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl
import net.bnb1.kradle.dsl.Value

class KotlinCodeAnalysisDsl(features: AllFeatures, properties: AllProperties) {

    val detekt = FeatureDsl(features.detekt, DetektDsl(properties))
    val detektConfigFile = Value("detekt-config.yml") { properties.detekt.configFile = it }
    val detektVersion = Value(Catalog.Versions.detekt) { properties.detekt.version = it }
}
