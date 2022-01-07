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
    private val _generalProperties by context { GeneralProperties(context, project) }
    val general = FeatureSetDsl(_general, _generalProperties)

    private val _jvm by context { JvmFeatureSet(project) }
    private val _jvmProperties by context { JvmProperties(context, project) }
    val jvm = FeatureSetDsl(_jvm, _jvmProperties)
}
