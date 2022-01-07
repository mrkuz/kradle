package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.FeatureDsl
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class JvmProperties(project: Project) : Properties() {

    private val javaBlueprint = JavaBlueprint(project)
    private val allOpenBlueprint = AllOpenBlueprint(project)

    val targetJvm = value(Catalog.Versions.jvm)

    val kotlin = FeatureDsl.Builder<KotlinProperties>(project)
        .feature { KotlinFeature() }
        .properties { KotlinProperties(it) }
        .parent(JvmFeatureSet::class)
        .addBlueprint(javaBlueprint)
        .addBlueprint(KotlinBlueprint(project))
        .addBlueprint(allOpenBlueprint)
        .build()
    val java = FeatureDsl.Builder<JavaProperties>(project)
        .feature { JavaFeature() }
        .properties { JavaProperties(it) }
        .parent(JvmFeatureSet::class)
        .addBlueprint(javaBlueprint)
        .build()
    val application = FeatureDsl.Builder<ApplicationProperties>(project)
        .feature { ApplicationFeature() }
        .properties { ApplicationProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(ApplicationBlueprint(project))
        .build()
    val library = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { LibraryFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(LibraryBlueprint(project))
        .addBlueprint(MavenPublishBlueprint(project))
        .build()
    val dependencyUpdates = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { DependencyUpdatesFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(DependencyUpdatesBlueprint(project))
        .build()
    val vulnerabilityScan = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { VulnerabilityScanFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(OwaspDependencyCheckBlueprint(project))
        .build()
    val lint = FeatureDsl.Builder<LintProperties>(project)
        .feature { LintFeature() }
        .properties { LintProperties() }
        .parent(JvmFeatureSet::class)
        // Make sure test an benchmark source sets are available
        .after(TestFeature::class, BenchmarkFeature::class)
        .addBlueprint(LintBlueprint(project))
        .build()
    val codeAnalysis = FeatureDsl.Builder<CodeAnalysisProperties>(project)
        .feature { CodeAnalysisFeature() }
        .properties { CodeAnalysisProperties() }
        .parent(JvmFeatureSet::class)
        .after(TestFeature::class, BenchmarkFeature::class)
        .addBlueprint(CodeAnalysisBlueprint(project))
        .build()
    val developmentMode = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { DevelopmentModeFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(DevelopmentModeBlueprint(project))
        .build()
    val devMode = developmentMode
    val test = FeatureDsl.Builder<TestProperties>(project)
        .feature { TestFeature() }
        .properties { TestProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(TestBlueprint(project))
        .addBlueprint(JacocoBlueprint(project))
        .build()
    val benchmark = FeatureDsl.Builder<BenchmarkProperties>(project)
        .feature { BenchmarkFeature() }
        .properties { BenchmarkProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(allOpenBlueprint)
        .addBlueprint(BenchmarksBlueprint(project))
        .build()

    @SuppressWarnings("VariableNaming")
    val `package` = FeatureDsl.Builder<PackageProperties>(project)
        .feature { PackageFeature() }
        .properties { PackageProperties(it) }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(PackageBlueprint(project))
        .addBlueprint(ShadowBlueprint(project))
        .build()
    val packaging = `package`
    val documentation = FeatureDsl.Builder<EmptyProperties>(project)
        .feature { DocumentationFeature() }
        .properties { EmptyProperties() }
        .parent(JvmFeatureSet::class)
        .addBlueprint(DokkaBlueprint(project))
        .build()
    val docker = FeatureDsl.Builder<DockerProperties>(project)
        .feature { DockerFeature() }
        .properties { DockerProperties() }
        .parent(JvmFeatureSet::class)
        .after(ApplicationFeature::class)
        .addBlueprint(JibBlueprint(project))
        .build()
}
