package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl
import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.dsl.Empty
import net.bnb1.kradle.dsl.Value

class JvmDsl(features: AllFeatures, properties: AllProperties) {

    val targetJvm = Value(properties.jvm.targetJvm) { properties.jvm.targetJvm = it }

    val kotlin = FeatureDsl(features.kotlin, KotlinDsl(features, properties))
    val java = FeatureDsl(features.java, JavaDsl(features, properties))
    val application = FeatureDsl(features.application, ApplicationDsl(properties.application))
    val library = FeatureDsl(features.library, Empty)
    val dependencyUpdates = FeatureDsl(features.dependencyUpdates, Empty)
    val dependencies = FeatureDsl(features.dependencies, DependenciesDsl(properties))
    val vulnerabilityScan = FeatureDsl(features.vulnerabilityScan, Empty)
    val lint = FeatureDsl(features.lint, LintDsl(properties.lint))
    val codeAnalysis = FeatureDsl(features.codeAnalysis, CodeAnalysisDsl(properties.codeAnalysis))
    val developmentMode = FeatureDsl(features.developmentMode, Empty)
    val devMode = developmentMode

    val test = FeatureDsl(features.test, TestDsl(features, properties))
    val codeCoverage = FeatureDsl(features.codeCoverage, CodeCoverageDsl(features, properties))
    val benchmark = FeatureDsl(features.benchmark, BenchmarkDsl(properties))

    @SuppressWarnings("VariableNaming")
    val `package` = FeatureDsl(features.packaging, PackagingDsl(properties))
    val packaging = `package`
    val docker = FeatureDsl(features.docker, DockerDsl(properties.jib))
    val documentation = FeatureDsl(features.documentation, Empty)
    val logging = FeatureDsl(features.logging, LoggingDsl(properties.logging))

    val frameworks = Configurable(FrameworksDsl(features, properties))
}
