package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Feature
import net.bnb1.kradle.features.FeatureRegistry

class JvmFeature : Feature() {

    override fun onEnable() = activate()
}