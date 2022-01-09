package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.support.tasks.BootstrapKotlinLibTask
import org.gradle.api.Project

class KotlinLibBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var extendsTask: String

    override fun doCreateTasks() {
        project.createHelperTask<BootstrapKotlinLibTask>(
            "bootstrapKotlinLib",
            "Bootstrap Kotlin library project"
        ).also {
            project.tasks.getByName(extendsTask).dependsOn(it)
        }
    }
}
