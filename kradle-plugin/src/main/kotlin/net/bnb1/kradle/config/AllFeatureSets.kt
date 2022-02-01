package net.bnb1.kradle.config

import net.bnb1.kradle.core.FeatureSet
import net.bnb1.kradle.support.Registry

class AllFeatureSets(registry: Registry) {

    val general = registry("general") { FeatureSet(it) }
    val jvm = registry("jvm") { FeatureSet(it) }
}
