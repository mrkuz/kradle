package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.support.tasks.BootstrapKotlinAppTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class KotlinAppBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties

    override fun doCreateTasks() {
        project.createHelperTask<BootstrapKotlinAppTask>(
            "bootstrapKotlinApp",
            "Bootstrap Kotlin application project"
        ).also {
            project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
        }
    }

    override fun doConfigure() {
        val mainClass = applicationProperties.mainClass.get()
        project.tasks.withType<BootstrapKotlinAppTask> {
            this.mainClass.set(mainClass)
        }
    }
}
