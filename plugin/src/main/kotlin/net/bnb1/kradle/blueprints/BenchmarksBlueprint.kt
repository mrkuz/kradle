package net.bnb1.kradle.blueprints

import kotlinx.benchmark.gradle.BenchmarksExtension
import kotlinx.benchmark.gradle.BenchmarksPlugin
import kotlinx.benchmark.gradle.JavaBenchmarkTarget
import kotlinx.benchmark.gradle.processJavaSourceSet
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
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

object BenchmarksBlueprint : PluginBlueprint<BenchmarksPlugin> {

    private const val SOURCE_SET_NAME = "benchmark"

    override fun configureEager(project: Project) {
        // JMH requires benchmark classes to be open
        project.configure<AllOpenExtension> {
            annotation("org.openjdk.jmh.annotations.State")
        }

        createSourceSet(project)

        project.alias("runBenchmarks", "Runs all JMH benchmarks", "benchmark")
    }

    override fun configure(project: Project, extension: KradleExtension) {
        val javaBenchmarkTarget = JavaBenchmarkTarget(
            project.extensions.getByType(BenchmarksExtension::class.java),
            SOURCE_SET_NAME,
            project.sourceSets.getByName(SOURCE_SET_NAME)
        )
        javaBenchmarkTarget.jmhVersion = extension.jmhVersion.get()
        project.processJavaSourceSet(javaBenchmarkTarget)

        project.tasks.named<Jar>("${SOURCE_SET_NAME}BenchmarkJar").configure {
            // Required workaround. Otherwise, running the benchmarks will complain because of
            // duplicate META-INF/versions/9/module-info.class
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    private fun createSourceSet(project: Project) {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        val benchmarkSourceSet = project.sourceSets.create(SOURCE_SET_NAME);

        benchmarkSourceSet.compileClasspath += mainSourceSet.output + mainSourceSet.compileClasspath
        benchmarkSourceSet.runtimeClasspath += mainSourceSet.output + mainSourceSet.runtimeClasspath
        @Suppress("DEPRECATION")
        benchmarkSourceSet.withConvention(KotlinSourceSet::class) {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:${BenchmarksPlugin.PLUGIN_VERSION}")
            }
        }
    }
}
