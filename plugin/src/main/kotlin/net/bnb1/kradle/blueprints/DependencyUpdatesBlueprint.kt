package net.bnb1.kradle.blueprints

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import net.bnb1.kradle.TaskBlueprint
import org.gradle.api.Project

object DependencyUpdatesBlueprint : TaskBlueprint<DependencyUpdatesTask> {

    override fun configure(project: Project, task: DependencyUpdatesTask) = task.apply {
        revision = "release"
        checkForGradleUpdate = true
        // Exclude milestones and RCs
        rejectVersionIf {
            val milestone = "^.*-M[0-9]+$".toRegex()
            val releaseCandidate = "^.*-RC$".toRegex()
            milestone.matches(candidate.version) || releaseCandidate.matches(candidate.version)
        }
    }
}