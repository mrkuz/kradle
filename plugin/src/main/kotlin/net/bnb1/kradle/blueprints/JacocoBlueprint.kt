package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.annotations.Invasive
import net.bnb1.kradle.create
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportsContainer

@Invasive
object JacocoBlueprint : PluginBlueprint<JacocoPlugin> {

    override fun configureEager(project: Project) {
        createTask(project, "integrationTest", "Generates code coverage report for integration tests")
        createTask(project, "functionalTest", "Generates code coverage report for functional tests")
    }

    override fun configure(project: Project, extension: KradleExtension) {
        project.configure<JacocoPluginExtension> {
            toolVersion = extension.tests.jacocoVersion.get()
        }

        project.tasks.named<JacocoReport>("jacocoTestReport").configure {
            dependsOn("test")
            configureReports(project, reports, "test")
        }
        project.tasks.named("test").configure {
            finalizedBy("jacocoTestReport")
        }
    }

    private fun createTask(project: Project, taskName: String, description: String) {
        val reportTaskName = "jacoco" + taskName.capitalize() + "Report"
        project.create(reportTaskName, description, JacocoReport::class.java).apply {
            dependsOn(taskName)
            sourceSets(
                project.extensions.getByType(SourceSetContainer::class.java).getByName(SourceSet.MAIN_SOURCE_SET_NAME)
            )
            executionData(project.tasks.getByName(taskName))
            configureReports(project, reports, taskName)
        }
        project.tasks.named(taskName).configure {
            finalizedBy(reportTaskName)
        }
    }

    private fun configureReports(project: Project, reports: JacocoReportsContainer, taskName: String) = reports.apply {
        csv.required.set(false)
        xml.required.set(false)
        html.outputLocation.set(project.buildDir.resolve("reports/jacoco/$taskName"))
    }
}