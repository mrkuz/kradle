package net.bnb1.kradle

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleBasePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.create("dependencyUpdates", DependencyUpdatesTask::class.java).apply {
            group = Constants.TASK_GROUP
            description = "Display dependency updates"
        }
    }
}