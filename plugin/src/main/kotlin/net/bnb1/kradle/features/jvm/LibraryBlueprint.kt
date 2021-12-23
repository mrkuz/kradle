package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

class LibraryBlueprint(project: Project) : Blueprint(project) {

    override fun checkPreconditions() {
        if (project.featureRegistry.get<ApplicationFeature>().isEnabled()) {
            throw GradleException("You can only enable 'application' or 'library' feature")
        }
    }

    override fun applyPlugins() {
        project.apply(JavaLibraryPlugin::class.java)
    }
}
