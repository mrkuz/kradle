package net.bnb1.kradle

import net.bnb1.kradle.dsl.FeatureSetDsl
import net.bnb1.kradle.features.general.GeneralFeatureSet
import net.bnb1.kradle.features.general.GeneralProperties
import net.bnb1.kradle.features.jvm.JvmFeatureSet
import net.bnb1.kradle.features.jvm.JvmProperties
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

open class KradleExtensionBase(project: Project) {

    init {
        project.extra["tracer"] = Tracer()

        val context = KradleContext()
        project.extra["context"] = context
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
