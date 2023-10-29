package net.bnb1.kradle.support.tasks

import net.bnb1.kradle.buildDirAsFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra

abstract class ProcessHelmChartTask : DefaultTask() {

    @get:InputDirectory
    val sourceDirectory = project.projectDir.resolve("src/main/helm")

    @get:OutputDirectory
    val outputDirectory = project.buildDirAsFile.resolve("helm")

    private val expandFiles = setOf("Chart.yaml", "values.yaml", "values-*.yaml")

    @TaskAction
    fun run() {
        project.copy {
            from(sourceDirectory)
            exclude(expandFiles)
            into(outputDirectory)
        }

        project.copy {
            from(sourceDirectory)
            include(expandFiles)
            expand(
                mapOf("project" to (project.properties + project.extra.properties))
            )
            into(outputDirectory)
        }
    }
}
