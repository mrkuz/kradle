package net.bnb1.kradle

import net.bnb1.kradle.config.AllFeatureSets
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.core.dsl.PresetDsl
import net.bnb1.kradle.presets.JavaApplicationPreset
import net.bnb1.kradle.presets.JavaLibraryPreset
import net.bnb1.kradle.presets.KotlinJvmApplicationPreset
import net.bnb1.kradle.presets.KotlinJvmLibraryPreset
import net.bnb1.kradle.support.Registry
import net.bnb1.kradle.support.Tracer
import java.util.concurrent.atomic.AtomicBoolean

open class KradleExtension(
    context: Registry,
    tracer: Tracer,
    featureSets: AllFeatureSets,
    features: AllFeatures,
    properties: AllProperties,
) : KradleExtensionDsl(tracer, featureSets, features, properties) {

    private val presetLock = AtomicBoolean()

    private val _kotlinJvmApplication = context { KotlinJvmApplicationPreset(this, presetLock) }
    val kotlinJvmApplication = PresetDsl(_kotlinJvmApplication)

    private val _kotlinJvmLibrary = context { KotlinJvmLibraryPreset(this, presetLock) }
    val kotlinJvmLibrary = PresetDsl(_kotlinJvmLibrary)

    private val _javaApplication = context { JavaApplicationPreset(this, presetLock) }
    val javaApplication = PresetDsl(_javaApplication)

    private val _javaLibrary = context { JavaLibraryPreset(this, presetLock) }
    val javaLibrary = PresetDsl(_javaLibrary)
}
