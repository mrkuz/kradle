package net.bnb1.kradle

import net.bnb1.kradle.features.AllFeatureSets
import net.bnb1.kradle.features.AllFeatures
import net.bnb1.kradle.features.AllProperties
import net.bnb1.kradle.features.FeatureSetDsl
import net.bnb1.kradle.features.general.GeneralDsl
import net.bnb1.kradle.features.jvm.JvmDsl
import net.bnb1.kradle.support.Tracer

open class KradleExtensionBase(
    tracer: Tracer,
    featureSets: AllFeatureSets,
    features: AllFeatures,
    properties: AllProperties
) {

    val general = FeatureSetDsl(tracer, featureSets.general, GeneralDsl(features))
    val jvm = FeatureSetDsl(tracer, featureSets.jvm, JvmDsl(features, properties))
}
