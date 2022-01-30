package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl
import net.bnb1.kradle.dsl.EmptyProperties

class JvmDsl(features: AllFeatures, properties: AllProperties) {

    val targetJvm = properties.jvm.targetJvm

    val kotlin = FeatureDsl(features.kotlin, KotlinDsl(properties))
    val java = FeatureDsl(features.java, JavaDsl(properties))
    val application = FeatureDsl(features.application, properties.application)
    val library = FeatureDsl(features.library, EmptyProperties)
    val dependencyUpdates = FeatureDsl(features.dependencyUpdates, EmptyProperties)
    val vulnerabilityScan = FeatureDsl(features.vulnerabilityScan, EmptyProperties)
    val lint = FeatureDsl(features.lint, properties.lint)
    val codeAnalysis = FeatureDsl(features.codeAnalysis, properties.codeAnalysis)
    val developmentMode = FeatureDsl(features.developmentMode, EmptyProperties)
    val devMode = developmentMode
    val test = FeatureDsl(features.test, properties.test)
    val benchmark = FeatureDsl(features.benchmark, properties.benchmark)

    @SuppressWarnings("VariableNaming")
    val `package` = FeatureDsl(features.packaging, PackagingDsl(properties))
    val packaging = `package`
    val docker = FeatureDsl(features.docker, properties.docker)
    val documentation = FeatureDsl(features.documentation, EmptyProperties)
}
