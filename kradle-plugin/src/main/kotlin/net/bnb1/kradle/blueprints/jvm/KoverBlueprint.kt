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

        // Kover only excludes test source set, but we have 'integrationTest', 'functionalTest', ...
        val testSourceSets = project.sourceSets.filter { it.name.endsWith("test", true) }
        val testSources = testSourceSets.flatMap { it.allSource.srcDirs }
        val testOutputs = testSourceSets.flatMap { it.output.classesDirs }
        project.tasks.withType<KoverHtmlReportTask> {
            srcDirs.set(srcDirs.get().filter { !testSources.contains(it) })
            outputDirs.set(outputDirs.get().filter { !testOutputs.contains(it) })
        }

        project.tasks.getByName(extendsTask).dependsOn("koverHtmlReport")
    }
}
