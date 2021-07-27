package net.bnb1.kradle.tasks

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

object DependencyUpdate : TaskBlueprint<DependencyUpdatesTask> {

    override val type = DependencyUpdatesTask::class.java

    override fun configure(task: DependencyUpdatesTask) = task.apply {
        revision = "release"
        checkForGradleUpdate = true
        // Exclude milestones and RCs
        rejectVersionIf {
            val milestone = "^.*-M[0-9]+$".toRegex()
            val releaseCandidate = "^.*-RC$".toRegex()
            milestone.matches(it.candidate.version) || releaseCandidate.matches(it.candidate.version)
        }
    }
}