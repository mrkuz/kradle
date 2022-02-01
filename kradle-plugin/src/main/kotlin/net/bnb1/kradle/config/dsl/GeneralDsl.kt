package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.core.dsl.FeatureDsl
import net.bnb1.kradle.dsl.EmptyProperties

class GeneralDsl(features: AllFeatures) {

    val bootstrap = FeatureDsl(features.bootstrap, EmptyProperties)
    val git = FeatureDsl(features.git, EmptyProperties)
    val projectProperties = FeatureDsl(features.projectProperties, EmptyProperties)
    val buildProperties = FeatureDsl(features.buildProperties, EmptyProperties)
}
