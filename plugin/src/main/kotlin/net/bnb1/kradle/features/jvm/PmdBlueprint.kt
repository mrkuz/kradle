package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.TargetJdk

private const val CONFIGURATION_NAME = "kradlePmd"

class PmdBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        val properties = project.propertiesRegistry.get<PmdProperties>()

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("${Catalog.Dependencies.Tools.pmd}:${properties.version.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val pmdTask = project.createHelperTask<Task>("pmd", "Runs PMD")
        project.tasks.getByName(CodeAnalysisFeature.MAIN_TASK).dependsOn(pmdTask)

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        javaExtension.sourceSets.forEach { sourceSet ->
            val taskName = "pmd" + sourceSet.name[0].toUpperCase() + sourceSet.name.substring(1)

            project.createHelperTask<Pmd>(taskName, "Runs PMD on '${sourceSet.name}'") {
                setSource(sourceSet.java.files)
                pmdClasspath = project.configurations.getAt(CONFIGURATION_NAME)
                classpath = project.objects.fileCollection().from(sourceSet.java.classesDirectory)
                reports {
                    html.required.set(true)
                    xml.required.set(false)
                }
                isConsoleOutput = true
                maxFailures.set(0)
                rulesMinimumPriority.set(5)
                incrementalAnalysis.set(true)
                ruleSetFiles = project.rootProject.files()
                ruleSets = properties.ruleSets.enabled.toList()
                targetJdk = TargetJdk.VERSION_1_7
                reports.forEach {
                    it.outputLocation.set(project.buildDir.resolve("reports/pmd/${sourceSet.name}.${it.name}"))
                }
            }
            pmdTask.dependsOn(taskName)
        }
    }
}
