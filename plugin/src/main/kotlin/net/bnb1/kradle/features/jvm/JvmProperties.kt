package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.FeatureDsl
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class JvmProperties(context: KradleContext, project: Project) : Properties() {

    private val _kotlinProperties by context { KotlinProperties(context) }
    private val _javaProperties by context { JavaProperties(context) }
    private val _applicationProperties by context { ApplicationProperties() }
    private val _lintProperties by context { LintProperties() }
    private val _codeAnalysisProperties by context { CodeAnalysisProperties() }
    private val _testProperties by context { TestProperties() }
    private val _benchmarkProperties by context { BenchmarkProperties() }
    private val _packageProperties by context { PackageProperties(context) }
    private val _dockerProperties by context { DockerProperties() }

    private val _javaBlueprint by context { JavaBlueprint(project) }
    private val _kotlinBlueprint by context { KotlinBlueprint(project) }
    private val _allOpenBlueprint by context { AllOpenBlueprint(project) }
    private val _applicationBlueprint by context { ApplicationBlueprint(project) }
    private val _libraryBlueprint by context { LibraryBlueprint(project) }
    private val _mavenPublishBlueprint by context { MavenPublishBlueprint(project) }
    private val _dependencyUpdatesBlueprint by context { DependencyUpdatesBlueprint(project) }
    private val _owaspDependencyCheckBlueprint by context { OwaspDependencyCheckBlueprint(project) }
    private val _lintBlueprint by context { LintBlueprint(project) }
    private val _codeAnalysisBlueprint by context { CodeAnalysisBlueprint(project) }
    private val _developmentModeBlueprint by context { DevelopmentModeBlueprint(project) }
    private val _testBlueprint by context { TestBlueprint(project) }
    private val _jacocoBlueprint by context { JacocoBlueprint(project) }
    private val _benchmarksBlueprint by context { BenchmarksBlueprint(project) }
    private val _packageBlueprint by context { PackageBlueprint(project) }
    private val _shadowBlueprint by context { ShadowBlueprint(project) }
    private val _dokkaBlueprint by context { DokkaBlueprint(project) }
    private val _jibBlueprint by context { JibBlueprint(project) }

    val targetJvm = value(Catalog.Versions.jvm)

    val kotlin = FeatureDsl.Builder<KotlinProperties>(project)
        .feature { KotlinFeature() }
        .properties { _kotlinProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_javaBlueprint)
        .addBlueprint(_kotlinBlueprint)
        .addBlueprint(_allOpenBlueprint)
        .build()
    val java = FeatureDsl.Builder<JavaProperties>(project)
        .feature { JavaFeature() }
        .properties { _javaProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_javaBlueprint)
        .build()
    val application = FeatureDsl.Builder<ApplicationProperties>(project)
        .feature { ApplicationFeature() }
        .properties { _applicationProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_applicationBlueprint)
        .build()
    val library = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { LibraryFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_libraryBlueprint)
        .addBlueprint(_mavenPublishBlueprint)
        .build()
    val dependencyUpdates = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { DependencyUpdatesFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_dependencyUpdatesBlueprint)
        .build()
    val vulnerabilityScan = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { VulnerabilityScanFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_owaspDependencyCheckBlueprint)
        .build()
    val lint = FeatureDsl.Builder<LintProperties>(project)
        .feature { LintFeature() }
        .properties { _lintProperties }
        .parent(JvmFeatureSet::class)
        // Make sure test an benchmark source sets are available
        .after(TestFeature::class, BenchmarkFeature::class)
        .addBlueprint(_lintBlueprint)
        .build()
    val codeAnalysis = FeatureDsl.Builder<CodeAnalysisProperties>(project)
        .feature { CodeAnalysisFeature() }
        .properties { _codeAnalysisProperties }
        .parent(JvmFeatureSet::class)
        .after(TestFeature::class, BenchmarkFeature::class)
        .addBlueprint(_codeAnalysisBlueprint)
        .build()
    val developmentMode = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { DevelopmentModeFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(_developmentModeBlueprint)
        .build()
    val devMode = developmentMode
    val test = FeatureDsl.Builder<TestProperties>(project)
        .feature { TestFeature() }
        .properties { _testProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_testBlueprint)
        .addBlueprint(_jacocoBlueprint)
        .build()
    val benchmark = FeatureDsl.Builder<BenchmarkProperties>(project)
        .feature { BenchmarkFeature() }
        .properties { _benchmarkProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_allOpenBlueprint)
        .addBlueprint(_benchmarksBlueprint)
        .build()

    @SuppressWarnings("VariableNaming")
    val `package` = FeatureDsl.Builder<PackageProperties>(project)
        .feature { PackageFeature() }
        .properties { _packageProperties }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(_packageBlueprint)
        .addBlueprint(_shadowBlueprint)
        .build()
    val packaging = `package`
    val documentation = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { DocumentationFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_dokkaBlueprint)
        .build()
    val docker = FeatureDsl.Builder<DockerProperties>(project)
        .feature { DockerFeature() }
        .properties { _dockerProperties }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(_jibBlueprint)
        .build()
}
