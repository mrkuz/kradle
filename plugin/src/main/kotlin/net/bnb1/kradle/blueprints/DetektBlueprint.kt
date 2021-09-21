package net.bnb1.kradle.blueprints

import io.gitlab.arturbosch.detekt.Detekt
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.create
import net.bnb1.kradle.plugins.NoOpPlugin
import net.bnb1.kradle.tasks.GenerateDetektConfigTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

object DetektBlueprint : PluginBlueprint<NoOpPlugin> {

    private const val CONFIGURATION_NAME = "kradleDetekt"
    private const val TASK_NAME = "analyzeCode"

    override fun configureEager(project: Project) {
        project.create<GenerateDetektConfigTask>("generateDetektConfig", "Generates detekt-config.yml")

        project.configurations.create(CONFIGURATION_NAME) {
            dependencies.add(project.dependencies.create("io.gitlab.arturbosch.detekt:detekt-cli:1.18.1"))
        }

        val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
        val sourceFiles = kotlinExtension.sourceSets
            .asSequence()
            .map { it.kotlin.files }
            .toSet()

        /*
        // Alternative approach
        @Suppress("DEPRECATION")
        val sourceFiles = project.sourceSets
            .asSequence()
            .map { it as HasConvention }
            .map { it.convention.getPlugin(KotlinSourceSet::class) }
            .flatMap { it.kotlin.files }
            .toSet()
        */

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

    override fun configure(project: Project, extension: KradleExtension) {
        val configFile = project.rootDir.resolve(extension.detektConfigFile.get())
        project.tasks.named<Detekt>(TASK_NAME).configure {
            if (configFile.exists()) {
                buildUponDefaultConfig = false
                config.setFrom(configFile)
            }
        }
    }
}