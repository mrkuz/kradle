package net.bnb1.kradle.blueprints

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.create
import net.bnb1.kradle.plugins.NoOpPlugin
import org.gradle.api.Project

object DependencyUpdatesBlueprint : PluginBlueprint<NoOpPlugin> {

    override fun configureEager(project: Project) {
        project.create<DependencyUpdatesTask>("showDependencyUpdates", "Displays dependency updates") {
            revision = "release"
            checkForGradleUpdate = true
            // Exclude milestones and RCs
            rejectVersionIf {
                val alpha = "^.*-alpha[.-]?[0-9]*$".toRegex()
                val milestone = "^.*[.-]M[.-]?[0-9]+$".toRegex()
                val releaseCandidate = "^.*-RC[.-]?[0-9]*$".toRegex()
                alpha.matches(candidate.version)
                        || milestone.matches(candidate.version)
                        || releaseCandidate.matches(candidate.version)
            }
        }
    }
}
