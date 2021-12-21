package net.bnb1.kradle.features.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.plugins.GitPlugin
import org.gradle.api.Project

class GitBlueprint(project : Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(GitPlugin::class.java)
    }
}