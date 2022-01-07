package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.tasks.BootstrapJavaAppTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class JavaAppBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties

    override fun createTasks() {
        project.createHelperTask<BootstrapJavaAppTask>(
            "bootstrapJavaApp",
            "Bootstrap Java application project"
        ).also {
            project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
        }
    }

    override fun configure() {
        val mainClass = applicationProperties.mainClass.get()
        project.tasks.withType<BootstrapJavaAppTask> {
            this.mainClass.set(mainClass)
        }
    }
}
