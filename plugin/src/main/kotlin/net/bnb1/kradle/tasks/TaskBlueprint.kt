package net.bnb1.kradle.tasks

import org.gradle.api.Task

interface TaskBlueprint<T : Task> {
    val type: Class<T>
    fun configure(task: T): T
}