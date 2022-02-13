package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.blueprints.jvm.DockerProperties
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createScriptTask
import net.bnb1.kradle.createTask
import net.bnb1.kradle.support.tasks.GenerateHelmChartTask
import net.bnb1.kradle.support.tasks.ProcessHelmChartTask
import org.gradle.api.Project

private const val PROCESS_TASK_NAME: String = "processHelmChart"

class HelmBlueprint(project: Project) : Blueprint(project) {

    lateinit var helmProperties: HelmProperties
    lateinit var dockerProperties: DockerProperties

    override fun doCreateTasks() {
        project.createTask<GenerateHelmChartTask>("generateHelmChart", "Generates Helm chart")
        project.createTask<ProcessHelmChartTask>(PROCESS_TASK_NAME, "Processes Helm chart")
        project.tasks.getByName("build").dependsOn(PROCESS_TASK_NAME)
    }

    override fun doCreateScriptTasks() {
        val releaseName = if (helmProperties.releaseName != null) {
            helmProperties.releaseName
        } else {
            project.rootProject.name
        }
        val chart = project.buildDir.resolve("helm").absolutePath

        project.createScriptTask("helmInstall", "Installs Helm chart") {
            commands.add(
                """
                helm install $releaseName $chart
                """.trimIndent()
            )
        }
        project.createScriptTask("helmUpgrade", "Upgrades Helm chart") {
            commands.add(
                """
                helm upgrade --install $releaseName $chart
                """.trimIndent()
            )
        }
        project.createScriptTask("helmUninstall", "Uninstalls Helm chart") {
            commands.add(
                """
                helm uninstall $releaseName
                """.trimIndent()
            )
        }
    }
}
