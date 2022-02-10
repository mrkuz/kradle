package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.dsl.Optional

class KotlinDsl(features: AllFeatures, properties: AllProperties) {

    val useCoroutines = Optional(Catalog.Versions.kotlinCoroutines) { properties.kotlin.useCoroutines = it }
    val kotlinxCoroutinesVersion = useCoroutines

    val lint = Configurable(KotlinLintDsl(features, properties))
    val codeAnalysis = Configurable(KotlinCodeAnalysisDsl(features, properties))
    val test = Configurable(KotlinTestDsl(properties))
}
