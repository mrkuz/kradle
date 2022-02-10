package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl
import net.bnb1.kradle.dsl.Value

class KotlinLintDsl(features: AllFeatures, properties: AllProperties) {

    val ktlint = FeatureDsl(features.ktlint, KtlintDsl(properties))
    val ktlintVersion = Value(Catalog.Versions.ktlint) { properties.ktlint.version = it }
}
