package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

class LibraryBlueprint(project: Project) : Blueprint(project) {

    override fun doApplyPlugins() {
        project.apply(JavaLibraryPlugin::class.java)
    }
}
