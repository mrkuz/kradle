package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.SourceSetContainer
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

inline fun <reified T : Task> Project.create(
    name: String,
    description: String,
    noinline configure: T.() -> Unit = {}
): T {
    return create(name, description, T::class.java, configure)
}

fun <T : Task> Project.create(name: String, description: String, type: Class<T>, configure: T.() -> Unit = {}): T {
    val task = tasks.create(name, type) { configure() }
    task.group = TASK_GROUP
    task.description = description
    return task
}

fun Project.alias(name: String, description: String, targetTask: String): Task {
    val task = create<Task>(name, description + " (alias for '${targetTask}')")
    task.dependsOn(targetTask)
    return task
}

val Project.extraDir
    get() = file(this.projectDir.resolve("src/main/extra"))

val Project.sourceSets
    get() = this.extensions.getByType(SourceSetContainer::class.java)

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
