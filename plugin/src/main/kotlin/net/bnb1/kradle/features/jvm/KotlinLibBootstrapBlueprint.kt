package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.tasks.BootstrapKotlinLibTask
import org.gradle.api.Project

class KotlinLibBootstrapBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        project.createHelperTask<BootstrapKotlinLibTask>(
            "bootstrapKotlinLib",
            "Bootstrap Kotlin library project"
        ).also {
            project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
        }
    }
}
