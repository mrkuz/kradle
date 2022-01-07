package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.FeatureDsl
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.features.general.BuildPropertiesBlueprint
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
        }
    }
    private val _kotlinBlueprint by context {
        KotlinBlueprint(project).inject {
            kotlinProperties = _kotlinProperties
            jvmProperties = this@JvmProperties
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
    private val _packageBlueprint by context { PackageBlueprint(project) }
    private val _packageApplicationBlueprint by context {
        PackageApplicationBlueprint(project).inject {
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
    private val _javaAppBoostrapBlueprint by context {
        JavaAppBootstrapBlueprint(project).inject {
            applicationProperties = _applicationProperties
        }
    }
    private val _javaLibBootstrapBlueprint by context { JavaLibBootstrapBlueprint(project) }
    private val _pmdBlueprint by context {
        PmdBlueprint(project).inject {
            pmdProperties = context.get()
            codeAnalysisProperties = _codeAnalysisProperties
        }
    }
    private val _spotBugsBlueprint by context {
        SpotBugsBlueprint(project).inject {
            spotBugsProperties = context.get()
            codeAnalysisProperties = _codeAnalysisProperties
        }
    }
    private val _checkstyleBlueprint by context {
        CheckstyleBlueprint(project).inject {
            checkstyleProperties = context.get()
            lintProperties = _lintProperties
        }
    }
    private val _kotlinAppBootstrapBlueprint by context {
        KotlinAppBootstrapBlueprint(project).inject {
            applicationProperties = _applicationProperties
        }
    }
    private val _kotlinLibBootstrapBlueprint by context { KotlinLibBootstrapBlueprint(project) }
    private val _detektBlueprint by context {
        DetektBlueprint(project).inject {
            detektProperties = context.get()
            codeAnalysisProperties = _codeAnalysisProperties
        }
    }
    private val _ktlintBlueprint by context {
        KtlintBlueprint(project).inject {
            ktlintProperties = context.get()
            lintProperties = _lintProperties
        }
    }
    private val _kotlinTestBlueprint by context {
        KotlinTestBlueprint(project).inject {
            kotlinTestProperties = context.get()
            testProperties = _testProperties
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

    init {
        _kotlin += setOf(
            _javaBlueprint,
            _kotlinBlueprint,
            _allOpenBlueprint,
            _kotlinAppBootstrapBlueprint,
            _kotlinLibBootstrapBlueprint,
        )
        _java += setOf(
            _javaBlueprint,
            _javaAppBoostrapBlueprint,
            _javaLibBootstrapBlueprint
        )
        _application += _applicationBlueprint
        _library += setOf(
            _libraryBlueprint,
            _mavenPublishBlueprint
        )
        _dependencyUpdates += _dependencyUpdatesBlueprint
        _vulnerabilityScan += _owaspDependencyCheckBlueprint
        _lint += setOf(
            _lintBlueprint,
            _ktlintBlueprint,
            _checkstyleBlueprint
        )
        _codeAnalysis += setOf(
            _codeAnalysisBlueprint,
            _detektBlueprint,
            _pmdBlueprint,
            _spotBugsBlueprint
        )
        _developmentMode += _developmentModeBlueprint
        _test += setOf(
            _testBlueprint,
            _jacocoBlueprint,
            _kotlinTestBlueprint
        )
        _benchmark += setOf(
            _allOpenBlueprint,
            _benchmarksBlueprint
        )
        _package += setOf(
            _packageBlueprint,
            _packageApplicationBlueprint,
            _shadowBlueprint
        )
        _docker += _jibBlueprint
        _documentation += _dokkaBlueprint

        _allOpenBlueprint.dependsOn += _kotlin
        _packageApplicationBlueprint.dependsOn += _application
        _shadowBlueprint.dependsOn += _application
        _javaAppBoostrapBlueprint.dependsOn += context.get<BootstrapFeature>()
        _javaAppBoostrapBlueprint.dependsOn += _application
        _javaLibBootstrapBlueprint.dependsOn += context.get<BootstrapFeature>()
        _javaLibBootstrapBlueprint.dependsOn += _library
        _checkstyleBlueprint.dependsOn += _java
        _pmdBlueprint.dependsOn += _java
        _spotBugsBlueprint.dependsOn += _java
        _kotlinAppBootstrapBlueprint.dependsOn += context.get<BootstrapFeature>()
        _kotlinAppBootstrapBlueprint.dependsOn += _application
        _kotlinLibBootstrapBlueprint.dependsOn += context.get<BootstrapFeature>()
        _kotlinLibBootstrapBlueprint.dependsOn += _library
        _ktlintBlueprint.dependsOn += _kotlin
        _detektBlueprint.dependsOn += _kotlin
        _spotBugsBlueprint.dependsOn += _java

        _application.conflictsWith = _library
        _library.conflictsWith = _application
        _developmentMode.requires = _application
        _docker.requires = _application

        // Make sure test and benchmark source sets are available
        _lint.after += _test
        _lint.after += _benchmark
        _codeAnalysis.after += _test
        _codeAnalysis.after += _benchmark

        _package.after += _application

        context.get<BuildPropertiesBlueprint>().also {
            _java += it
            _kotlin += it
        }

        context.get<JvmFeatureSet>() += setOf(
            _kotlin,
            _java,
            _application,
            _library,
            _dependencyUpdates,
            _vulnerabilityScan,
            _lint,
            _codeAnalysis,
            _developmentMode,
            _test,
            _benchmark,
            _package,
            _docker,
            _documentation
        )
    }

    val targetJvm = value(Catalog.Versions.jvm)

    val kotlin = FeatureDsl(_kotlin, _kotlinProperties)
    val java = FeatureDsl(_java, _javaProperties)
    val application = FeatureDsl(_application, _applicationProperties)
    val library = FeatureDsl(_library, EmptyProperties())
    val dependencyUpdates = FeatureDsl(_dependencyUpdates, EmptyProperties())
    val vulnerabilityScan = FeatureDsl(_vulnerabilityScan, EmptyProperties())
    val lint = FeatureDsl(_lint, _lintProperties)
    val codeAnalysis = FeatureDsl(_codeAnalysis, _codeAnalysisProperties)
    val developmentMode = FeatureDsl(_developmentMode, EmptyProperties())
    val devMode = developmentMode
    val test = FeatureDsl(_test, _testProperties)
    val benchmark = FeatureDsl(_benchmark, _benchmarkProperties)

    @SuppressWarnings("VariableNaming")
    val `package` = FeatureDsl(_package, _packageProperties)
    val packaging = `package`
    val docker = FeatureDsl(_docker, _dockerProperties)
    val documentation = FeatureDsl(_documentation, EmptyProperties())
}
