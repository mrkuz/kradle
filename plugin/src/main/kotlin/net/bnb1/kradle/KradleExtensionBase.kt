package net.bnb1.kradle

import net.bnb1.kradle.dsl.FeatureSetDsl
import net.bnb1.kradle.features.general.GeneralFeatureSet
import net.bnb1.kradle.features.general.GeneralProperties
import net.bnb1.kradle.features.jvm.JvmFeatureSet
import net.bnb1.kradle.features.jvm.JvmProperties
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

open class KradleExtensionBase(context: KradleContext, project: Project) {

    init {
        project.extra["tracer"] = Tracer()
        project.extra["context"] = context
    }

    private val _general by context { GeneralFeatureSet(project) }
    val general = FeatureSetDsl.Builder<GeneralProperties>(project)
        .featureSet { _general }
        .properties { GeneralProperties(context, it) }
        .build()

    private val _jvm by context { JvmFeatureSet(project) }
    val jvm = FeatureSetDsl.Builder<JvmProperties>(project)
        .featureSet { _jvm }
        .properties { JvmProperties(context, it) }
        .build()
}
