package net.bnb1.kradle.config.dsl.general

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl
import net.bnb1.kradle.dsl.Empty

class GeneralDsl(features: AllFeatures, properties: AllProperties) {

    val bootstrap = FeatureDsl(features.bootstrap, Empty)
    val git = FeatureDsl(features.git, Empty)
    val buildProfiles = FeatureDsl(features.buildProfiles, BuildProfilesDsl(properties.buildProfiles))
    val projectProperties = FeatureDsl(features.projectProperties, Empty)
    val buildProperties = FeatureDsl(features.buildProperties, Empty)
    val scripts = FeatureDsl(features.scripts, ScriptsDsl(properties.scripts))
    val helm = FeatureDsl(features.helm, HelmDsl(properties.helm))
}
