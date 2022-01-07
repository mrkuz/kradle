package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties

class KotlinProperties(context: KradleContext) : Properties() {

    val kotlinxCoroutinesVersion = optional<String>()
    fun useCoroutines(version: String = Catalog.Versions.kotlinCoroutines) = kotlinxCoroutinesVersion.set(version)

    private val _lintProperties by context { KotlinLintProperties(context) }
    val lint = PropertiesDsl(_lintProperties)

    private val _codeAnalysisProperties by context { KotlinCodeAnalysisProperties(context) }
    val codeAnalysis = PropertiesDsl(_codeAnalysisProperties)

    private val _testProperties by context { KotlinTestProperties() }
    val test = PropertiesDsl(_testProperties)
}
