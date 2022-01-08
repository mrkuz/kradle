package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.AllFeatures
import net.bnb1.kradle.features.AllProperties
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.FeatureDsl

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
    val `package` = FeatureDsl(features.packaging, properties.packaging)
    val packaging = `package`
    val docker = FeatureDsl(features.docker, properties.docker)
    val documentation = FeatureDsl(features.documentation, EmptyProperties)
}
