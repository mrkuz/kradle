package net.bnb1.kradle.features.general

import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.gradle.api.Task

class BootstrapBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        project.createTask<Task>(BootstrapFeature.MAIN_TASK, "Bootstraps project")
    }
}
