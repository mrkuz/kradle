package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.create
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.gradle.api.Task

class LintBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        project.create(LintFeature.MAIN_TASK, "Run linters", Task::class.java)
        project.tasks.getByName("check").dependsOn(LintFeature.MAIN_TASK)
    }
}
