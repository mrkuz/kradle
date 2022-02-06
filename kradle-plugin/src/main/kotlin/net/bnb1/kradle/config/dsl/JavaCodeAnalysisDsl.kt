package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class JavaCodeAnalysisDsl(features: AllFeatures, properties: AllProperties) {

    val pmd = FeatureDsl(features.pmd, properties.pmd)

    val spotbugs = FeatureDsl(features.spotBugs, properties.spotBugs)
    val spotBugs = spotbugs
}
