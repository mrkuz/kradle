package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class JavaCodeAnalysisDsl(features: AllFeatures, properties: AllProperties) {

    val pmd = FeatureDsl(features.pmd, PmdDsl(properties))

    val spotbugs = FeatureDsl(features.spotBugs, SpotBugsDsl(properties))
    val spotBugs = spotbugs
}
