package net.bnb1.kradle.features.general

import net.bnb1.kradle.dsl.FeatureDsl
import net.bnb1.kradle.features.AllFeatures
import net.bnb1.kradle.features.EmptyProperties

class GeneralDsl(features: AllFeatures) {

    val bootstrap = FeatureDsl(features.bootstrap, EmptyProperties)
    val git = FeatureDsl(features.git, EmptyProperties)
    val projectProperties = FeatureDsl(features.projectProperties, EmptyProperties)
    val buildProperties = FeatureDsl(features.buildProperties, EmptyProperties)
}
