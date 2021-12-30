package net.bnb1.kradle.features.jvm

import io.gitlab.arturbosch.detekt.Detekt
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.tasks.GenerateDetektConfigTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

private const val CONFIGURATION_NAME = "kradleDetekt"
private const val TASK_NAME = "analyzeCode"

class DetektBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        val properties = project.propertiesRegistry.get<KotlinCodeAnalysisProperties>()

        project.createTask<GenerateDetektConfigTask>("generateDetektConfig", "Generates detekt-config.yml") {
            outputFile.set(project.rootDir.resolve(properties.detektConfigFile.get()))
        }

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("io.gitlab.arturbosch.detekt:detekt-cli:${properties.detektVersion.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
        val sourceFiles = kotlinExtension.sourceSets
            .asSequence()
            .flatMap { it.kotlin.files }
            .filter { it.extension.toLowerCase() == "kt" }
            .toSet()

        project.createTask<Detekt>(TASK_NAME, "Runs detekt code analysis") {
            setSource(sourceFiles)
            detektClasspath.setFrom(project.configurations.getAt(CONFIGURATION_NAME))
            reports {
                html.required.set(true)
                xml.required.set(false)
                sarif.required.set(false)
                txt.required.set(false)
            }
        }

        project.tasks.getByName("check").dependsOn(TASK_NAME)
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<KotlinCodeAnalysisProperties>()
        val configFile = project.rootDir.resolve(properties.detektConfigFile.get())
        project.tasks.named<Detekt>(TASK_NAME).configure {
            if (configFile.exists()) {
                buildUponDefaultConfig = false
                config.setFrom(configFile)
            }
        }
    }
}
