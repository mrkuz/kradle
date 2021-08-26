package net.bnb1.kradle.blueprints

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

object DependencyUpdatesBlueprint : PluginBlueprint<VersionsPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
            revision = "release"
            checkForGradleUpdate = true
            // Exclude milestones and RCs
            rejectVersionIf {
                val alpha = "^.*-alpha[.-]?[0-9]*$".toRegex()
                val milestone = "^.*-M[.-]?[0-9]+$".toRegex()
                val releaseCandidate = "^.*-RC[.-]?[0-9]*$".toRegex()
                alpha.matches(candidate.version)
                        || milestone.matches(candidate.version)
                        || releaseCandidate.matches(candidate.version)
            }
        }

        project.alias("showDependencyUpdates", "Displays dependency updates", "dependencyUpdates")
    }
}