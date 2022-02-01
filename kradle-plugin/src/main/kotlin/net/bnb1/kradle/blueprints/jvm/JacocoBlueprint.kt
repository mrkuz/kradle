package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportsContainer

class JacocoBlueprint(project: Project) : Blueprint(project) {

    lateinit var jacocoProperties: JacocoProperties

    override fun doApplyPlugins() {
        project.apply(JacocoPlugin::class.java)
    }

    override fun doCreateTasks() {
        jacocoProperties.includes.get().forEach {
            if (it != "test") {
                createTask(it, "Generates code coverage report for '$it'")
            }
        }
    }

    private fun createTask(taskName: String, description: String) {
        val reportTaskName = "jacoco" + taskName.capitalize() + "Report"
        project.createHelperTask<JacocoReport>(reportTaskName, description) {
            dependsOn(taskName)
            sourceSets(
                project.extensions.getByType(SourceSetContainer::class.java).getByName(SourceSet.MAIN_SOURCE_SET_NAME)
            )
            executionData(project.tasks.getByName(taskName))
            configureReports(reports, taskName)
        }
        project.tasks.named(taskName).configure {
            finalizedBy(reportTaskName)
        }
    }

    override fun doConfigure() {
        project.configure<JacocoPluginExtension> {
            toolVersion = jacocoProperties.version.get()
        }

        project.tasks.named<JacocoReport>("jacocoTestReport").configure {
            dependsOn("test")
            configureReports(reports, "test")
        }
        project.tasks.named("test").configure {
            finalizedBy("jacocoTestReport")
        }
    }

    private fun configureReports(reports: JacocoReportsContainer, taskName: String) = reports.apply {
        csv.required.set(false)
        xml.required.set(false)
        html.outputLocation.set(project.buildDir.resolve("reports/jacoco/$taskName"))
    }
}
