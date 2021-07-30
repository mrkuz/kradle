package net.bnb1.kradle

import com.adarshr.gradle.testlogger.TestLoggerPlugin
import net.bnb1.kradle.blueprints.*
import net.bnb1.kradle.tasks.HelloWorldTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin


class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.repositories {
            mavenCentral()
            gradlePluginPortal()
            mavenLocal()
        }

        project.apply(KotlinBlueprint)
        project.apply(MavenPublishBlueprint)
        project.apply(AllOpenGradleSubplugin::class.java)
        project.apply(BenchmarksBlueprint)
        project.apply(TestLoggerPlugin::class.java)

        project.create("showDependencyUpdates", "Displays dependency updates", DependencyUpdatesBlueprint)
        project.create("generateDocumentation", "Generates HTML documentation", DokkaHtmlBlueprint)
        project.alias("runBenchmarks", "Runs all benchmarks", "benchmark")
        project.alias("package", "Creates JAR", "jar")
        project.alias("install", "Installs JAR to local Maven repository", "publishToMavenLocal")
        project.create("helloWorld", "Displays hello world", HelloWorldTask::class.java)

        project.configure("test", TestBlueprint)
    }
}
