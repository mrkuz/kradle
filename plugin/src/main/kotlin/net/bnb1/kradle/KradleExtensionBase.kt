package net.bnb1.kradle

import net.bnb1.kradle.features.AllFeatures
import net.bnb1.kradle.features.AllProperties
import net.bnb1.kradle.features.FeatureSetDsl
import net.bnb1.kradle.features.general.GeneralDsl
import net.bnb1.kradle.features.general.GeneralFeatureSet
import net.bnb1.kradle.features.jvm.JvmDsl
import net.bnb1.kradle.features.jvm.JvmFeatureSet
import net.bnb1.kradle.support.Tracer
import org.gradle.api.Project

open class KradleExtensionBase(
    context: KradleContext,
    tracer: Tracer,
    features: AllFeatures,
    properties: AllProperties,
    project: Project
) {

    private val _general by context {
        GeneralFeatureSet().also {
            it += setOf(
                features.bootstrap,
                features.git,
                features.projectProperties,
                features.buildProperties
            )
        }
    }
    val general = FeatureSetDsl(tracer, _general, GeneralDsl(features))

    private val _jvm by context {
        JvmFeatureSet().also {
            it += setOf(
                features.kotlin,
                features.java,
                features.application,
                features.library,
                features.dependencyUpdates,
                features.vulnerabilityScan,
                features.lint,
                features.codeAnalysis,
                features.developmentMode,
                features.test,
                features.benchmark,
                features.packaging,
                features.docker,
                features.documentation
            )
        }
    }
    val jvm = FeatureSetDsl(tracer, _jvm, JvmDsl(features, properties))
}
