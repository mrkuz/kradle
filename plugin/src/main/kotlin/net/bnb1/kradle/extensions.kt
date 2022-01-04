package net.bnb1.kradle

import net.bnb1.kradle.features.FeatureRegistry
import net.bnb1.kradle.features.FeatureSetRegistry
import net.bnb1.kradle.features.PropertiesRegistry
import net.bnb1.kradle.presets.PresetRegistry
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.extra

const val KRADLE_TASK_GROUP = "Kradle"
const val HELPER_TASK_GROUP = "Kradle helper"

// Project

fun <T : Plugin<out Project>> Project.apply(type: Class<T>) = pluginManager.apply(type)

inline fun <reified T : Task> Project.createTask(
    name: String,
    description: String,
    noinline configure: T.() -> Unit = {}
): T {
    return createTask(KRADLE_TASK_GROUP, name, description, T::class.java, configure)
}

inline fun <reified T : Task> Project.createHelperTask(
    name: String,
    description: String,
    noinline configure: T.() -> Unit = {}
): T {
    return createTask(HELPER_TASK_GROUP, name, description, T::class.java, configure)
}

fun <T : Task> Project.createTask(
    group: String,
    name: String,
    description: String,
    type: Class<T>,
    configure: T.() -> Unit = {}
): T {
    val task = tasks.create(name, type) { configure() }
    task.group = group
    task.description = description
    return task
}

fun Project.alias(name: String, description: String, targetTask: String): Task {
    val task = createTask<Task>(name, "$description (alias for '$targetTask')")
    task.dependsOn(targetTask)
    return task
}

val Project.extraDir
    get() = file(this.projectDir.resolve("src/main/extra"))

val Project.sourceSets
    get() = this.extensions.getByType(SourceSetContainer::class.java)

val Project.tracer
    get() = this.extra.get("tracer") as Tracer

val Project.featureRegistry
    get() = this.extra.get("featureRegistry") as FeatureRegistry

val Project.propertiesRegistry
    get() = this.extra.get("propertiesRegistry") as PropertiesRegistry

val Project.presetRegistry
    get() = this.extra.get("presetRegistry") as PresetRegistry

val Project.featureSetRegistry
    get() = this.extra.get("featureSetRegistry") as FeatureSetRegistry

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
