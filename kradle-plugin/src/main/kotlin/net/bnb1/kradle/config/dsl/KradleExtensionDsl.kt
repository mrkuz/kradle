package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatureSets
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureSetDsl
import net.bnb1.kradle.support.Tracer

open class KradleExtensionDsl(
    tracer: Tracer,
    featureSets: AllFeatureSets,
    features: AllFeatures,
    properties: AllProperties
) {

    val general = FeatureSetDsl(tracer, featureSets.general, GeneralDsl(features))
    val jvm = FeatureSetDsl(tracer, featureSets.jvm, JvmDsl(features, properties))
}
