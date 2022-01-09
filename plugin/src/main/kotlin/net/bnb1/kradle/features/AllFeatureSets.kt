package net.bnb1.kradle.features

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.core.FeatureSet

class AllFeatureSets(context: KradleContext) {

    val general = context("general") { FeatureSet(it) }
    val jvm = context("jvm") { FeatureSet(it) }
}
