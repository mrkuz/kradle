package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.support.plugins.GitPlugin
import org.gradle.api.Project

class GitBlueprint(project: Project) : Blueprint(project) {

    lateinit var extendsBootstrapTask: String

    override fun doApplyPlugins() {
        project.apply(GitPlugin::class.java)
    }

    override fun doConfigure() {
        project.tasks.findByName(extendsBootstrapTask)?.dependsOn(GitPlugin.TASK_NAME)
    }
}
