package net.bnb1.kradle.blueprints

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

object DependencyUpdatesBlueprint : PluginBlueprint<VersionsPlugin> {

    override fun configure(project: Project) {
        project.tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
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
}