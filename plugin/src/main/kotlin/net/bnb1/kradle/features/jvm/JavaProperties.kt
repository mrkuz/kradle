package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties

class JavaProperties(context: KradleContext) : Properties() {

    val previewFeatures = flag()

    private val _lintProperties by context { JavaLintProperties(context) }
    val lint = PropertiesDsl(_lintProperties)

    private val _codeAnalysisProperties by context { JavaCodeAnalysisProperties(context) }
    val codeAnalysis = PropertiesDsl(_codeAnalysisProperties)
}
