package net.bnb1.kradle

import com.adarshr.gradle.testlogger.TestLoggerPlugin
import net.bnb1.kradle.blueprints.*
import net.bnb1.kradle.tasks.GenerateBuildPropertiesTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin

class KradleBasePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.extensions.create<KradleExtension>("kradle")

        project.repositories {
            mavenCentral()
            gradlePluginPortal()
            mavenLocal()
        }

        try {
            Class.forName("org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper")
        } catch (e: ClassNotFoundException) {
            throw GradleException("Kotlin plugin not found")
        }

        project.apply(KotlinBlueprint)

        project.apply(AllOpenGradleSubplugin::class.java)
        project.apply(SerializationGradleSubplugin::class.java)

        project.apply(DependencyUpdatesBlueprint)
        project.apply(DokkaBlueprint)
        project.apply(BenchmarksBlueprint)
        project.apply(TestLoggerPlugin::class.java)
        project.apply(JacocoBlueprint)
        project.apply(DependencyCheckBlueprint)
        project.apply(KtlintBlueprint)
        project.apply(DetektBlueprint)

        project.apply(GitPlugin::class.java)
        project.apply(ProjectPropertiesPlugin::class.java)

        project.alias("showDependencyUpdates", "Displays dependency updates", "dependencyUpdates")
        project.alias("analyzeDependencies", "Analyzes dependencies for vulnerabilities", "dependencyCheckAnalyze")
        project.alias("generateDocumentation", "Generates Dokka HTML documentation", "dokkaHtml")
        project.alias("runBenchmarks", "Runs all JMH benchmarks", "benchmark")
        project.alias("lint", "Runs ktlint", "ktlintCheck")
        project.alias("analyzeCode", "Runs detekt code analysis", "detekt")
        project.alias("package", "Creates JAR", "jar")

        project.create("generateBuildProperties", "Generates build.properties", GenerateBuildPropertiesTask::class.java)

        project.configure("test", TestBlueprint)
    }
}
