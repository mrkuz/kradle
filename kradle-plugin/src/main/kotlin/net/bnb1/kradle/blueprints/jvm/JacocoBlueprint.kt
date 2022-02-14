package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportsContainer
import java.io.File
import java.util.concurrent.Callable

class JacocoBlueprint(project: Project) : Blueprint(project) {

    lateinit var jacocoProperties: JacocoProperties
    lateinit var extendsTask: String

    override fun doApplyPlugins() {
        project.apply(JacocoPlugin::class.java)
    }

    override fun doCreateTasks() {
        val testTasks = project.tasks.withType(Test::class.java)
            .filter { !jacocoProperties.excludes.contains(it.name) }

        val executionData = testTasks.asSequence()
            .map { it.extensions.getByType(JacocoTaskExtension::class.java) }
            .map { it.destinationFile }
            .filterNotNull()
            .map { Callable<File?> { if (it.exists()) it else null } }
            .toList()

        val jacocoTask = project.createHelperTask<JacocoReport>(
            "jacocoHtmlReport",
            "Generates JaCoCo code coverage HTML report"
        ) {
            dependsOn(testTasks)
            sourceSets(project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME))
            executionData(executionData)
            configureReports(reports)
        }
        project.tasks.findByName(extendsTask)?.dependsOn(jacocoTask)
    }

    override fun doConfigure() {
        project.configure<JacocoPluginExtension> {
            toolVersion = jacocoProperties.version
        }

        project.tasks.named<JacocoReport>("jacocoTestReport").configure {
            dependsOn("test")
        }

        project.tasks.withType<Test> {
            val extension = extensions.getByType(JacocoTaskExtension::class.java)
            extension.isEnabled = !jacocoProperties.excludes.contains(name)
        }
    }

    private fun configureReports(reports: JacocoReportsContainer) = reports.apply {
        csv.required.set(false)
        xml.required.set(false)
        html.outputLocation.set(project.buildDir.resolve("reports/jacoco/project-html/"))
    }
}
