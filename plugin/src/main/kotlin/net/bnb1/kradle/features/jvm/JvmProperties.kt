package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.FeatureDsl
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.inject
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

    private val _javaBlueprint by context {
        JavaBlueprint(project).inject {
            javaProperties = _javaProperties
            jvmProperties = this@JvmProperties
            applicationProperties = _applicationProperties
            lintProperties = _lintProperties
            codeAnalysisProperties = _codeAnalysisProperties
            checkstyleProperties = context.get()
            pmdProperties = context.get()
            spotBugsProperties = context.get()
        }
    }
    private val _kotlinBlueprint by context {
        KotlinBlueprint(project).inject {
            kotlinProperties = _kotlinProperties
            jvmProperties = this@JvmProperties
            applicationProperties = _applicationProperties
            lintProperties = _lintProperties
            codeAnalysisProperties = _codeAnalysisProperties
            testProperties = _testProperties
            detektProperties = context.get()
            ktlintProperties = context.get()
            kotlinTestProperties = context.get()
        }
    }
    private val _allOpenBlueprint by context { AllOpenBlueprint(project) }
    private val _applicationBlueprint by context {
        ApplicationBlueprint(project).inject {
            applicationProperties = _applicationProperties
            javaProperties = _javaProperties
        }
    }
    private val _libraryBlueprint by context { LibraryBlueprint(project) }
    private val _mavenPublishBlueprint by context { MavenPublishBlueprint(project) }
    private val _dependencyUpdatesBlueprint by context { DependencyUpdatesBlueprint(project) }
    private val _owaspDependencyCheckBlueprint by context { OwaspDependencyCheckBlueprint(project) }
    private val _lintBlueprint by context { LintBlueprint(project) }
    private val _codeAnalysisBlueprint by context { CodeAnalysisBlueprint(project) }
    private val _developmentModeBlueprint by context {
        DevelopmentModeBlueprint(project).inject {
            applicationProperties = _applicationProperties
            javaProperties = _javaProperties
        }
    }
    private val _testBlueprint by context {
        TestBlueprint(project).inject {
            testProperties = _testProperties
            javaProperties = _javaProperties
        }
    }
    private val _jacocoBlueprint by context {
        JacocoBlueprint(project).inject {
            testProperties = _testProperties
        }
    }
    private val _benchmarksBlueprint by context {
        BenchmarksBlueprint(project).inject {
            benchmarkProperties = _benchmarkProperties
            javaProperties = _javaProperties
        }
    }
    private val _packageBlueprint by context {
        PackageBlueprint(project).inject {
            applicationProperties = _applicationProperties
        }
    }
    private val _shadowBlueprint by context {
        ShadowBlueprint(project).inject {
            uberJarProperties = context.get()
        }
    }
    private val _dokkaBlueprint by context { DokkaBlueprint(project) }
    private val _jibBlueprint by context {
        JibBlueprint(project).inject {
            dockerProperties = _dockerProperties
            applicationProperties = _applicationProperties
        }
    }

    private val _kotlin by context { KotlinFeature() }
    private val _java by context { JavaFeature() }
    private val _application by context { ApplicationFeature() }
    private val _library by context { LibraryFeature() }
    private val _dependencyUpdates by context { DependencyUpdatesFeature() }
    private val _vulnerabilityScan by context { VulnerabilityScanFeature() }
    private val _lint by context { LintFeature() }
    private val _codeAnalysis by context { CodeAnalysisFeature() }
    private val _developmentMode by context { DevelopmentModeFeature() }
    private val _test by context { TestFeature() }
    private val _benchmark by context { BenchmarkFeature() }
    private val _package by context { PackageFeature() }
    private val _docker by context { DockerFeature() }
    private val _documentation by context { DocumentationFeature() }

    val targetJvm = value(Catalog.Versions.jvm)

    val kotlin = FeatureDsl.Builder<KotlinProperties>(project)
        .feature { _kotlin }
        .properties { _kotlinProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_javaBlueprint)
        .addBlueprint(_kotlinBlueprint)
        .addBlueprint(_allOpenBlueprint)
        .build()
    val java = FeatureDsl.Builder<JavaProperties>(project)
        .feature { _java }
        .properties { _javaProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_javaBlueprint)
        .build()
    val application = FeatureDsl.Builder<ApplicationProperties>(project)
        .feature { _application }
        .properties { _applicationProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_applicationBlueprint)
        .build()
    val library = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _library }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_libraryBlueprint)
        .addBlueprint(_mavenPublishBlueprint)
        .build()
    val dependencyUpdates = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _dependencyUpdates }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_dependencyUpdatesBlueprint)
        .build()
    val vulnerabilityScan = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _vulnerabilityScan }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_owaspDependencyCheckBlueprint)
        .build()
    val lint = FeatureDsl.Builder<LintProperties>(project)
        .feature { _lint }
        .properties { _lintProperties }
        .parent(JvmFeatureSet::class)
        // Make sure test an benchmark source sets are available
        .after(TestFeature::class, BenchmarkFeature::class)
        .addBlueprint(_lintBlueprint)
        .build()
    val codeAnalysis = FeatureDsl.Builder<CodeAnalysisProperties>(project)
        .feature { _codeAnalysis }
        .properties { _codeAnalysisProperties }
        .parent(JvmFeatureSet::class)
        .after(TestFeature::class, BenchmarkFeature::class)
        .addBlueprint(_codeAnalysisBlueprint)
        .build()
    val developmentMode = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _developmentMode }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(_developmentModeBlueprint)
        .build()
    val devMode = developmentMode
    val test = FeatureDsl.Builder<TestProperties>(project)
        .feature { _test }
        .properties { _testProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_testBlueprint)
        .addBlueprint(_jacocoBlueprint)
        .build()
    val benchmark = FeatureDsl.Builder<BenchmarkProperties>(project)
        .feature { _benchmark }
        .properties { _benchmarkProperties }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_allOpenBlueprint)
        .addBlueprint(_benchmarksBlueprint)
        .build()

    @SuppressWarnings("VariableNaming")
    val `package` = FeatureDsl.Builder<PackageProperties>(project)
        .feature { _package }
        .properties { _packageProperties }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(_packageBlueprint)
        .addBlueprint(_shadowBlueprint)
        .build()
    val packaging = `package`
    val docker = FeatureDsl.Builder<DockerProperties>(project)
        .feature { _docker }
        .properties { _dockerProperties }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(_jibBlueprint)
        .build()
    val documentation = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { _documentation }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(_dokkaBlueprint)
        .build()
}
