package net.bnb1.kradle.plugins

import net.bnb1.kradle.create
import net.bnb1.kradle.tasks.GenerateGitignoreTask
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants;
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

class GitPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (project.rootDir.resolve(".git").exists()) {
            val git = Git.open(project.rootDir)
            val hash = git.repository.findRef(Constants.HEAD).objectId?.name
            if (hash != null) {
                project.extra["gitCommit"] = hash.substring(0, 7)
            }
        }

        project.create("generateGitignore", "Generates .gitignore", GenerateGitignoreTask::class.java)
    }
}