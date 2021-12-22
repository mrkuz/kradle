package net.bnb1.kradle.features.jvm

import io.gitlab.arturbosch.detekt.Detekt
import net.bnb1.kradle.create
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
        project.create<GenerateDetektConfigTask>("generateDetektConfig", "Generates detekt-config.yml")

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                val properties = project.propertiesRegistry.get<KotlinCodeAnalysisProperties>()
                project.dependencies.create("io.gitlab.arturbosch.detekt:detekt-cli:${properties.detektVersion.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
        val sourceFiles = kotlinExtension.sourceSets
            .asSequence()
            .map { it.kotlin.files }
            .toSet()

        project.create<Detekt>(TASK_NAME, "Runs detekt code analysis") {
            setSource(sourceFiles)
            detektClasspath.setFrom(project.configurations.getAt(CONFIGURATION_NAME))
            reports {
                html.enabled = true
                xml.enabled = false
                sarif.enabled = false
                txt.enabled = false
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
