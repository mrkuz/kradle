package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

const val TASK_GROUP = "Kradle"

inline fun <reified T : Plugin<Project>> Project.apply(blueprint: PluginBlueprint<T>) {
    pluginManager.apply(T::class.java)
    blueprint.configure(this)
}

inline fun <reified T : Task> Project.create(name: String, description: String, blueprint: TaskBlueprint<T>): T {
    return blueprint.configure(this, create(name, description, T::class.java))
}

fun <T : Task> Project.create(name: String, description: String, type: Class<T>): T {
    val task = tasks.create(name, type)
    task.group = TASK_GROUP
    task.description = description
    return task
}