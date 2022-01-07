package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.tasks.BootstrapKotlinAppTask
import net.bnb1.kradle.tasks.BootstrapKotlinLibTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class KotlinBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties

    override fun createTasks() {
        if (project.featureRegistry.get<ApplicationFeature>().isEnabled) {
            project.createHelperTask<BootstrapKotlinAppTask>(
                "bootstrapKotlinApp",
                "Bootstrap Kotlin application project"
            ).also {
                project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
            }
        } else if (project.featureRegistry.get<LibraryFeature>().isEnabled) {
            project.createHelperTask<BootstrapKotlinLibTask>(
                "bootstrapKotlinLib",
                "Bootstrap Kotlin library project"
            ).also {
                project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
            }
        }
    }

    override fun configure() {
        if (project.featureRegistry.get<ApplicationFeature>().isEnabled) {
            val mainClass = applicationProperties.mainClass.get()
            project.tasks.withType<BootstrapKotlinAppTask> {
                this.mainClass.set(mainClass)
            }
        }
    }
}
