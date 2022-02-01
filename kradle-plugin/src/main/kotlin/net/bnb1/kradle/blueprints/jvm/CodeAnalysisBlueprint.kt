package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import org.gradle.api.Project
import org.gradle.api.Task

class CodeAnalysisBlueprint(project: Project) : Blueprint(project) {

    lateinit var taskName: String

    override fun doCreateTasks() {
        project.createTask<Task>(taskName, "Runs code analysis")
        project.tasks.getByName("check").dependsOn(taskName)
    }
}
