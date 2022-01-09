package net.bnb1.kradle.features.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.support.plugins.GitPlugin
import org.gradle.api.Project

class GitBlueprint(project: Project) : Blueprint(project) {

    override fun doApplyPlugins() {
        project.apply(GitPlugin::class.java)
    }
}
