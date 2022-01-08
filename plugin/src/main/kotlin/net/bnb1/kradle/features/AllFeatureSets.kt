package net.bnb1.kradle.features

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.features.general.GeneralFeatureSet
import net.bnb1.kradle.features.jvm.JvmFeatureSet

class AllFeatureSets(context: KradleContext) {

    val general by context { GeneralFeatureSet() }
    val jvm by context { JvmFeatureSet() }
}
