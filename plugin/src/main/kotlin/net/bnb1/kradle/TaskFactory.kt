package net.bnb1.kradle

import org.gradle.api.Project
import org.gradle.api.Task

class TaskFactory(private val project: Project) {

    inline fun <reified T : Task> create(name: String, description: String, blueprint: TaskBlueprint<T>): T {
        return blueprint.configure(create(name, description, T::class.java))
    }

    fun <T : Task> create(name: String, description: String, type: Class<T>): T {
        val task = project.tasks.create(name, type)
        task.group = TASK_GROUP
        task.description = description
        return task
    }

    companion object {
        const val TASK_GROUP = "Kradle"
    }
}