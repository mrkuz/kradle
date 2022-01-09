package net.bnb1.kradle.features.general

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import org.gradle.api.Project
import org.gradle.api.Task

class BootstrapBlueprint(project: Project) : Blueprint(project) {

    override fun doCreateTasks() {
        project.createTask<Task>(BootstrapFeature.MAIN_TASK, "Bootstraps project")
    }
}
