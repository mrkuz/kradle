package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.DependencyHandlerScope

const val TASK_GROUP = "Kradle"

// Project

inline fun <reified T : Plugin<Project>> Project.apply(blueprint: PluginBlueprint<T>) {
    pluginManager.apply(T::class.java)
    blueprint.configureEager(this)
    afterEvaluate {
        val extension = project.extensions.getByType(KradleExtension::class.java)
        if (!extension.isDisabled(blueprint)) {
            blueprint.configure(this, extension)
        }
    }
}

fun <T : Plugin<Project>> Project.apply(type: Class<T>) = pluginManager.apply(type)

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

val Project.extraDir
    get() = file(this.projectDir.resolve("src/main/extra"))

// DependencyHandlerScope

fun DependencyHandlerScope.implementation(notation: Any) = add("implementation", notation)
fun DependencyHandlerScope.testImplementation(notation: Any) = add("testImplementation", notation)
fun DependencyHandlerScope.testRuntimeOnly(notation: Any) = add("testRuntimeOnly", notation)

// ObjectFactory

inline fun <reified T> ObjectFactory.property(default: T): Property<T> {
    return property(T::class.java).apply {
        convention(default)
    }
}

inline fun <reified T> ObjectFactory.empty(): Property<T> = property(T::class.java)
