package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import org.gradle.api.Project
import org.gradle.api.Task

class BootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var taskName: String

    override fun doCreateTasks() {
        project.createTask<Task>(taskName, "Bootstraps project")
    }
}
