package net.bnb1.kradle

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllFeatureSets
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllPresets
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.core.dsl.PresetDsl
import net.bnb1.kradle.support.Tracer

@Suppress("LeakingThis")
open class KradleExtension(
    tracer: Tracer,
    featureSets: AllFeatureSets,
    features: AllFeatures,
    blueprints: AllBlueprints,
    properties: AllProperties,
    presets: AllPresets
) : KradleExtensionDsl(tracer, featureSets, features, blueprints, properties) {

    val kotlinJvmApplication = PresetDsl(this, presets.kotlinJvmApplication)
    val kotlinJvmLibrary = PresetDsl(this, presets.kotlinJvmLibrary)
    val javaApplication = PresetDsl(this, presets.javaApplication)
    val javaLibrary = PresetDsl(this, presets.javaLibrary)
}
