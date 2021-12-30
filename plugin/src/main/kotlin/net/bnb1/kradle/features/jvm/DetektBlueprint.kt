package net.bnb1.kradle.features.jvm

import io.gitlab.arturbosch.detekt.Detekt
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.sourceSets
import net.bnb1.kradle.tasks.GenerateDetektConfigTask
import org.gradle.api.Project

private const val CONFIGURATION_NAME = "kradleDetekt"

class DetektBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        val properties = project.propertiesRegistry.get<KotlinCodeAnalysisProperties>()
        val configFile = project.rootDir.resolve(properties.detektConfigFile.get())

        project.createTask<GenerateDetektConfigTask>("generateDetektConfig", "Generates detekt-config.yml") {
            outputFile.set(project.rootDir.resolve(properties.detektConfigFile.get()))
        }

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("io.gitlab.arturbosch.detekt:detekt-cli:${properties.detektVersion.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        project.sourceSets.forEach { sourceSet ->
            val sourceFiles = sourceSet.allSource.files
                .filter { it.extension.toLowerCase() == "kt" }
                .toSet()

            val taskName = "detekt" + sourceSet.name[0].toUpperCase() + sourceSet.name.substring(1)

            project.createHelperTask<Detekt>(taskName, "Runs detekt code analysis on '${sourceSet.name}'") {
                setSource(sourceFiles)
                detektClasspath.setFrom(project.configurations.getAt(CONFIGURATION_NAME))
                reports {
                    html {
                        required.set(true)
                        outputLocation.set(project.buildDir.resolve("reports/detekt/${sourceSet.name}.html"))
                    }
                    xml.required.set(false)
                    sarif.required.set(false)
                    txt.required.set(false)
                }
                if (configFile.exists()) {
                    buildUponDefaultConfig = false
                    config.setFrom(configFile)
                }
            }

            project.tasks.getByName(CodeAnalysisFeature.MAIN_TASK).dependsOn(taskName)
        }
    }
}
