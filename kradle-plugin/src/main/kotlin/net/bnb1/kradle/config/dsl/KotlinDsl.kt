package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class KotlinDsl(features: AllFeatures, properties: AllProperties) {

    val kotlinxCoroutinesVersion = properties.kotlin.kotlinxCoroutinesVersion
    fun useCoroutines(version: String = Catalog.Versions.kotlinCoroutines) = kotlinxCoroutinesVersion.set(version)

    val lint = Configurable(KotlinLintDsl(features, properties))
    val codeAnalysis = Configurable(KotlinCodeAnalysisDsl(features, properties))
    val test = Configurable(properties.kotlinTest)
}
