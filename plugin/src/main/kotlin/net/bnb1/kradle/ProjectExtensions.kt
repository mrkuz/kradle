package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.named

const val TASK_GROUP = "Kradle"

inline fun <reified T : Plugin<Project>> Project.apply(blueprint: PluginBlueprint<T>) {
    pluginManager.apply(T::class.java)
    blueprint.configure(this)
}

fun <T : Plugin<Project>> Project.apply(type: Class<T>) {
    pluginManager.apply(type)
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

fun Project.create(name: String, description: String): Task {
    val task = tasks.create(name)
    task.group = TASK_GROUP
    task.description = description
    return task
}

fun Project.alias(name: String, description: String, targetTask: String): Task {
    val task = create(name, description + " (alias for '${targetTask}')")
    task.dependsOn(targetTask)
    return task
}

inline fun <reified T : Task> Project.configure(name: String, blueprint: TaskBlueprint<T>) {
    tasks.named<T>(name).configure {
        blueprint.configure(this@configure, this)
    }
}