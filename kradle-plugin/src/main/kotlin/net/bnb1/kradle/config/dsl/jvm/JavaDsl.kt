package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Optional

class JavaDsl(features: AllFeatures, properties: AllProperties) {

    val previewFeatures = Flag { properties.java.previewFeatures = it }
    val withLombok = Optional(Catalog.Versions.lombok) { properties.java.withLombok = it }
    val lint = Configurable(JavaLintDsl(features, properties))
    val codeAnalysis = Configurable(JavaCodeAnalysisDsl(features, properties))
}
