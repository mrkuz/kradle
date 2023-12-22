package net.bnb1.kradle.blueprints.jvm

import kotlinx.benchmark.gradle.BenchmarksExtension
import kotlinx.benchmark.gradle.BenchmarksPlugin
import kotlinx.benchmark.gradle.JavaBenchmarkTarget
import kotlinx.benchmark.gradle.processJavaSourceSet
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.alias
import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

private const val SOURCE_SET_NAME = "benchmark"

class BenchmarksBlueprint(project: Project) : Blueprint(project) {

    lateinit var jmhProperties: JmhProperties
    lateinit var javaProperties: JavaProperties

    lateinit var withBuildProfiles: () -> Boolean

    override fun shouldActivate(): Boolean {
        if (javaProperties.previewFeatures) {
            project.logger.warn("WARNING: Benchmarks are currently not working with preview features enabled")
            return false
        }
        return true
    }

    override fun doApplyPlugins() {
        project.apply(BenchmarksPlugin::class.java)
    }

    override fun doCreateSourceSets() {
        project.extensions.findByType<AllOpenExtension>()?.apply {
            // JMH requires benchmark classes to be open
            annotation("org.openjdk.jmh.annotations.State")
        }

        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val benchmarkSourceSet = project.sourceSets.create(SOURCE_SET_NAME)

        benchmarkSourceSet.compileClasspath += mainSourceSet.output + mainSourceSet.compileClasspath
        benchmarkSourceSet.runtimeClasspath += mainSourceSet.output + mainSourceSet.runtimeClasspath

        val runtime = project.dependencies.create(
            "${Catalog.Dependencies.Tools.kotlinxBenchmarkRuntime}:${BenchmarksPlugin.PLUGIN_VERSION}"
        )
        val configuration = project.configurations.getByName(benchmarkSourceSet.implementationConfigurationName)
        configuration.dependencies.add(runtime)
    }

    override fun doAddAliases() {
        project.alias("runBenchmarks", "Runs all JMH benchmarks", "benchmark")
    }

    override fun doConfigure() {
        val javaBenchmarkTarget = JavaBenchmarkTarget(
            project.extensions.getByType(BenchmarksExtension::class.java),
            SOURCE_SET_NAME,
            project.sourceSets.getByName(SOURCE_SET_NAME)
        )

        javaBenchmarkTarget.jmhVersion = jmhProperties.version
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
