package net.bnb1.kradle

import net.bnb1.kradle.features.FeatureRegistry
import net.bnb1.kradle.features.FeatureSetDsl
import net.bnb1.kradle.features.PropertiesRegistry
import net.bnb1.kradle.features.general.GeneralFeatureSet
import net.bnb1.kradle.features.general.GeneralProperties
import net.bnb1.kradle.features.jvm.JvmFeatureSet
import net.bnb1.kradle.features.jvm.JvmProperties
import net.bnb1.kradle.presets.PresetRegistry
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

open class KradleExtensionBase(project: Project) {

    init {
        project.extra["featureRegistry"] = FeatureRegistry()
        project.extra["propertiesRegistry"] = PropertiesRegistry()
        project.extra["presetRegistry"] = PresetRegistry()
    }

    val general = FeatureSetDsl.Builder<GeneralProperties>(project)
        .featureSet { GeneralFeatureSet(it) }
        .properties { GeneralProperties(it) }
        .build()
    val jvm = FeatureSetDsl.Builder<JvmProperties>(project)
        .featureSet { JvmFeatureSet(it) }
        .properties { JvmProperties(it) }
        .build()
}
