package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.create
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

object JacocoBlueprint : PluginBlueprint<JacocoPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.configure<JacocoPluginExtension> {
            toolVersion = extension.tests.jacocoVersion.get()
        }

        project.tasks.named<JacocoReport>("jacocoTestReport").configure {
            dependsOn("test")
            reports {
                csv.required.set(false)
                xml.required.set(false)
                html.outputLocation.set(project.buildDir.resolve("reports/jacoco/test"))
            }
        }
        project.tasks.named("test").configure {
            finalizedBy("jacocoTestReport")
        }

        createReportTask(project, "integrationTest", "Generates code coverage report for integration tests")
        createReportTask(project, "functionalTest", "Generates code coverage report for functional tests")
    }

    private fun createReportTask(project: Project, taskName: String, description: String) {
        val reportTaskName = "jacoco" + taskName.capitalize() + "Report"
        project.create(
            reportTaskName, description, JacocoReport::class.java
        ).apply {
            dependsOn(taskName)
            sourceSets(
                project.extensions.getByType(SourceSetContainer::class.java).getByName(SourceSet.MAIN_SOURCE_SET_NAME)
            )
            executionData(project.tasks.getByName(taskName))
            reports {
                csv.required.set(false)
                xml.required.set(false)
                html.outputLocation.set(project.buildDir.resolve("reports/jacoco/$taskName"))
            }
        }
        project.tasks.named(taskName).configure {
            finalizedBy(reportTaskName)
        }
    }
}