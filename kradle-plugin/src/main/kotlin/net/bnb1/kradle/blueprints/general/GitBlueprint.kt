package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.support.plugins.GitPlugin
import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import org.gradle.api.Task

class GitBlueprint(project: Project) : Blueprint(project) {

    lateinit var extendsBootstrapTask: String

    override fun doApplyPlugins() {
        project.apply(GitPlugin::class.java)
    }

    override fun doCreateTasks() {
        val task = project.createHelperTask<Task>("stageAllFiles", "Stage all files") {
            doLast {
                val git = if (!project.rootDir.resolve(".git").exists()) {
                    Git.init().setDirectory(project.rootDir).call()
                } else {
                    Git.open(project.rootDir)
                }
                git.add().addFilepattern(".").call()
            }
        }

        project.tasks.findByName(extendsBootstrapTask)?.apply {
            dependsOn(GitPlugin.TASK_NAME)
            finalizedBy(task)
        }
    }
}
