package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class JavaDsl(properties: AllProperties) {

    val previewFeatures = properties.java.previewFeatures
    val lint = Configurable(JavaLintDsl(properties))
    val codeAnalysis = Configurable(JavaCodeAnalysisDsl(properties))
}
