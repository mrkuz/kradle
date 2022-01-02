package net.bnb1.kradle.features.jvm

import kotlinx.benchmark.gradle.BenchmarksExtension
import kotlinx.benchmark.gradle.BenchmarksPlugin
import kotlinx.benchmark.gradle.JavaBenchmarkTarget
import kotlinx.benchmark.gradle.processJavaSourceSet
import net.bnb1.kradle.alias
import net.bnb1.kradle.apply
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withConvention
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

private const val SOURCE_SET_NAME = "benchmark"

class BenchmarksBlueprint(project: Project) : Blueprint(project) {

    override fun shouldActivate(): Boolean {
        val javaProperties = project.propertiesRegistry.get<JavaProperties>()
        if (javaProperties.withPreviewFeatures.get()) {
            project.logger.warn("WARNING: Benchmarks are currently not working with preview features enabled")
            return false
        }
        return true
    }

    override fun applyPlugins() {
        project.apply(BenchmarksPlugin::class.java)
    }

    // compat: Must be public we can create the tasks eagerly
    public override fun createSourceSets() {
        // compat: Avoid duplicate creation on activate
        if (project.sourceSets.findByName(SOURCE_SET_NAME) != null) {
            return
        }

        if (project.featureRegistry.get<KotlinFeature>().isEnabled) {
            // JMH requires benchmark classes to be open
            project.configure<AllOpenExtension> {
                annotation("org.openjdk.jmh.annotations.State")
            }
        }

        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val benchmarkSourceSet = project.sourceSets.create(SOURCE_SET_NAME)

        benchmarkSourceSet.compileClasspath += mainSourceSet.output + mainSourceSet.compileClasspath
        benchmarkSourceSet.runtimeClasspath += mainSourceSet.output + mainSourceSet.runtimeClasspath
        @Suppress("DEPRECATION")
        benchmarkSourceSet.withConvention(KotlinSourceSet::class) {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:${BenchmarksPlugin.PLUGIN_VERSION}")
            }
        }
    }

    override fun addAliases() {
        project.alias("runBenchmarks", "Runs all JMH benchmarks", "benchmark")
    }

    override fun configure() {
        val javaBenchmarkTarget = JavaBenchmarkTarget(
            project.extensions.getByType(BenchmarksExtension::class.java),
            SOURCE_SET_NAME,
            project.sourceSets.getByName(SOURCE_SET_NAME)
        )

        val properties = project.propertiesRegistry.get<BenchmarkProperties>()
        javaBenchmarkTarget.jmhVersion = properties.jmhVersion.get()
        project.afterEvaluate {
            project.processJavaSourceSet(javaBenchmarkTarget)

            project.tasks.named<Jar>("${SOURCE_SET_NAME}BenchmarkJar").configure {
                // Required workaround. Otherwise, running the benchmarks will complain because of
                // duplicate META-INF/versions/9/module-info.class
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
    }
}
