package net.bnb1.kradle.blueprints

import kotlinx.benchmark.gradle.BenchmarksExtension
import kotlinx.benchmark.gradle.BenchmarksPlugin
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

        project.configure<BenchmarksExtension> { targets.register("$SOURCE_SET_NAME") }
        project.alias("runBenchmarks", "Runs all JMH benchmarks", "benchmark")
    }

    override fun configure(project: Project, extension: KradleExtension) {
        project.tasks.named<Jar>("${SOURCE_SET_NAME}BenchmarkJar").configure {
            // Required workaround. Otherwise, running the benchmarks will complain because of
            // duplicate META-INF/versions/9/module-info.class
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    private fun createSourceSet(project: Project) {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        val benchmarkSourceSet = project.sourceSets.create("$SOURCE_SET_NAME");

        benchmarkSourceSet.compileClasspath += mainSourceSet.output + mainSourceSet.compileClasspath
        benchmarkSourceSet.runtimeClasspath += mainSourceSet.output + mainSourceSet.runtimeClasspath
        @Suppress("DEPRECATION")
        benchmarkSourceSet.withConvention(KotlinSourceSet::class) {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.3.1")
            }
        }
    }
}
