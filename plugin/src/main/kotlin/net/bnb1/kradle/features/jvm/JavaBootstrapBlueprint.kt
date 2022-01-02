package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.tasks.BootstrapJavaAppTask
import net.bnb1.kradle.tasks.BootstrapJavaLibTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class JavaBootstrapBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        if (project.featureRegistry.get<ApplicationFeature>().isEnabled) {
            project.createHelperTask<BootstrapJavaAppTask>(
                "bootstrapJavaApp", "Bootstrap Java application project"
            ).also {
                project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
            }
        } else if (project.featureRegistry.get<LibraryFeature>().isEnabled) {
            project.createHelperTask<BootstrapJavaLibTask>(
                "bootstrapJavaLib", "Bootstrap Java library project"
            ).also {
                project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
            }
        }
    }

    override fun configure() {
        if (project.featureRegistry.get<ApplicationFeature>().isEnabled) {
            val mainClass = project.propertiesRegistry.get<ApplicationProperties>().mainClass.get()
            project.tasks.withType<BootstrapJavaAppTask> {
                this.mainClass.set(mainClass)
            }
        }
    }
}
