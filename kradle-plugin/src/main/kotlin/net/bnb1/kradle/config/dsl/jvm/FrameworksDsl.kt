package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class FrameworksDsl(features: AllFeatures, properties: AllProperties) {

    val springBoot = FeatureDsl(features.springBoot, SpringBootDsl(properties.springBoot))
}
