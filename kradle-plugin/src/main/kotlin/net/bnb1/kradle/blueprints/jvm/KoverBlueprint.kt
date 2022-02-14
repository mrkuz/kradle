package net.bnb1.kradle.blueprints.jvm

import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.KoverTaskExtension
import kotlinx.kover.tasks.KoverHtmlReportTask
import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

class KoverBlueprint(project: Project) : Blueprint(project) {

    lateinit var koverProperties: KoverProperties
    lateinit var extendsTask: String

    override fun doApplyPlugins() {
        project.apply(KoverPlugin::class.java)
    }

    override fun doConfigure() {
        project.tasks.withType<Test> {
            val extension = extensions.getByType(KoverTaskExtension::class.java)
            extension.isDisabled = koverProperties.excludes.contains(name)
        }

        // Kover only excludes the "test" source set, but we want to exclude all test variants and benchmarks.
        val excludeSourceSets = project.sourceSets.filter {
            it.name.endsWith("test", true) || it.name == "benchmark"
        }
        val excludeSources = excludeSourceSets.flatMap { it.allSource.srcDirs }
        val excludeOutputs = excludeSourceSets.flatMap { it.output.classesDirs }
        project.tasks.withType<KoverHtmlReportTask> {
            srcDirs.set(srcDirs.get().filter { !excludeSources.contains(it) })
            outputDirs.set(outputDirs.get().filter { !excludeOutputs.contains(it) })
        }

        project.tasks.getByName(extendsTask).dependsOn("koverHtmlReport")
    }
}
