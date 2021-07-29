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

inline fun <reified T : Task> Project.configure(name: String, blueprint: TaskBlueprint<T>) {
    tasks.named<T>(name).configure {
        blueprint.configure(this@configure, this)
    }
}