package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.support.tasks.BootstrapKotlinAppTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class KotlinAppBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties
    lateinit var extendsTask: String

    override fun doCreateTasks() {
        project.createHelperTask<BootstrapKotlinAppTask>(
            "bootstrapKotlinApp",
            "Bootstrap Kotlin application project"
        ).also {
            project.tasks.getByName(extendsTask).dependsOn(it)
        }
    }

    override fun doConfigure() {
        val mainClass = applicationProperties.mainClass.get()
        project.tasks.withType<BootstrapKotlinAppTask> {
            this.mainClass.set(mainClass)
        }
    }
}
