package net.bnb1.kradle.support.plugins

import net.bnb1.kradle.createTask
import net.bnb1.kradle.support.tasks.GenerateGitignoreTask
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

private const val COMMIT_LENGTH = 7

class GitPlugin : Plugin<Project> {

    companion object {
        const val TASK_NAME = "generateGitignore"
    }

    override fun apply(project: Project) {
        if (project.rootDir.resolve(".git").exists()) {
            val git = Git.open(project.rootDir)
            val hash = git.repository.findRef(Constants.HEAD).objectId?.name
            if (hash != null) {
                project.extra["gitCommit"] = hash.substring(0, COMMIT_LENGTH)
            }
            git.repository.branch?.let {
                project.extra["gitBranch"] = it
                val index = it.indexOfAny(setOf("/", "-", "_"))
                if (index == -1) {
                    project.extra["gitBranchPrefix"] = project.extra["gitBranch"]
                } else {
                    project.extra["gitBranchPrefix"] = it.substring(0, index)
                }
            }
        }

        project.createTask<GenerateGitignoreTask>(TASK_NAME, "Generates .gitignore")
    }
}
