package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class KotlinLintDsl(features: AllFeatures, properties: AllProperties) {

    val ktlint = FeatureDsl(features.ktlint, properties.ktlint)
    val ktlintVersion = properties.ktlint.version
}
