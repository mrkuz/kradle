package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.gradle.api.Task

class LintBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        project.createTask<Task>(LintFeature.MAIN_TASK, "Runs linters")
        project.tasks.getByName("check").dependsOn(LintFeature.MAIN_TASK)
    }
}
