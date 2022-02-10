package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class CodeCoverageDsl(features: AllFeatures, properties: AllProperties) {

    val jacoco = FeatureDsl(features.jacoco, JacocoDsl(properties))
    val kover = FeatureDsl(features.kover, KoverDsl(properties))
}
