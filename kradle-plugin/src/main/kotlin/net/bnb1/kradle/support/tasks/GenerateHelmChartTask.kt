package net.bnb1.kradle.support.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateHelmChartTask : DefaultTask() {

    private val outputDirectory: File = project.projectDir.resolve("src/main/helm")

    init {
        outputs.upToDateWhen { outputDirectory.exists() }
    }

    @TaskAction
    fun run() {
        if (!outputDirectory.exists()) {
            with(outputDirectory) {
                mkdirs()
                resolve("charts").mkdir()
                resolve("charts/.gitkeep").createNewFile()
                resolve("templates").mkdir()

                copyTextResource("Chart.yaml")
                copyTextResource("values.yaml")
                copyTextResource("helmignore", ".helmignore")
                copyTextResource("templates/_helpers.tpl")
                copyTextResource("templates/deployment.yaml")
                copyTextResource("templates/ingress.yaml")
                copyTextResource("templates/NOTES.txt")
                copyTextResource("templates/service.yaml")
                copyTextResource("templates/serviceaccount.yaml")
            }
        }
    }

    private fun copyTextResource(resource: String, to: String = resource) {
        outputDirectory.resolve(to).writeText(javaClass.getResource("/helm/$resource")!!.readText())
    }
}
