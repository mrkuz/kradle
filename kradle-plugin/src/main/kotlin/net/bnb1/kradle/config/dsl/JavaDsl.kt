package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class JavaDsl(features: AllFeatures, properties: AllProperties) {

    val previewFeatures = properties.java.previewFeatures
    val withLombok = properties.java.withLombok
    val lint = Configurable(JavaLintDsl(features, properties))
    val codeAnalysis = Configurable(JavaCodeAnalysisDsl(features, properties))
}
