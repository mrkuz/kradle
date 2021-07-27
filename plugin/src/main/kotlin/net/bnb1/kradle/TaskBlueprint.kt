package net.bnb1.kradle

import org.gradle.api.Task

interface TaskBlueprint<T : Task> {

    fun configure(task: T): T
}