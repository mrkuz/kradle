package net.bnb1.kradle.features.jvm

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentFilter
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project

class DependencyUpdatesBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        project.createTask<DependencyUpdatesTask>("showDependencyUpdates", "Displays dependency updates") {
            revision = "release"
            checkForGradleUpdate = true
            rejectVersionIf(VersionFilter)
        }
    }

    object VersionFilter : ComponentFilter {

        // Exclude milestones and RCs
        override fun reject(current: ComponentSelectionWithCurrent?) = reject(current!!.candidate.version)

        fun reject(version: String): Boolean {
            val alpha = "^.*[.-]alpha[.-]?[0-9]*$".toRegex()
            val milestone = "^.*[.-]M[.-]?[0-9]+$".toRegex()
            val releaseCandidate = "^.*[.-]RC[.-]?[0-9]*$".toRegex()
            return alpha.matches(version) ||
                milestone.matches(version) ||
                releaseCandidate.matches(version)
        }
    }
}
