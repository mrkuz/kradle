package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class CodeCoverageDsl(features: AllFeatures, properties: AllProperties) {

    val jacoco = FeatureDsl(features.jacoco, properties.jacoco)
    val kover = FeatureDsl(features.kover, properties.kover)
}
