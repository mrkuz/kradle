package net.bnb1.kradle.blueprints.jvm

import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.KoverProjectConfig
import kotlinx.kover.api.KoverTaskExtension
import net.bnb1.kradle.apply
import net.bnb1.kradle.buildDirAsFile
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
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
            extension.isDisabled.set(koverProperties.excludes.contains(name))
        }

        // Exclude all test and benchmark source sets
        val excludeSourceSets = project.sourceSets
            .filter {
                it.name.endsWith("test", true) || it.name == "benchmark"
            }
            .map { it.name }

        project.configure<KoverProjectConfig> {
            filters {
                sourceSets {
                    excludes.addAll(excludeSourceSets)
                }
            }
            htmlReport {
                reportDir.set(project.buildDirAsFile.resolve("reports/kover/project-html/"))
            }
        }

        project.tasks.getByName(extendsTask).dependsOn("koverHtmlReport")
    }
}
