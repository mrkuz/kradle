package net.bnb1.kradle

import org.gradle.api.Project
import org.gradle.api.Task

interface TaskBlueprint<T : Task> {

    fun configure(project: Project, task: T): T = task
}