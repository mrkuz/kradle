package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.features.AllProperties

class JavaDsl(properties: AllProperties) {

    val lint = Configurable(JavaLintDsl(properties))
    val codeAnalysis = Configurable(JavaCodeAnalysisDsl(properties))
}
