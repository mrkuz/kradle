package net.bnb1.kradle.tasks

import org.eclipse.jgit.api.Git
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AbstractBoostrapTask : DefaultTask() {

    init {
        dependsOn(":wrapper", "generateGitignore", "generateDetektConfig")
    }

    @TaskAction
    fun stageOne() {
        if (!project.rootDir.resolve(".git").exists()) {
            Git.init().setDirectory(project.rootDir).call()
        }

        listOf("kotlin", "resources", "extra").forEach {
            project.projectDir.resolve("src/main/$it").mkdirs()
        }
        listOf("kotlin", "resources").forEach {
            project.projectDir.resolve("src/test/$it").mkdirs()
        }
        project.projectDir.resolve("src/benchmark/kotlin").mkdirs()

        listOf("README.md", "LICENSE", "project.properties").forEach {
            project.rootDir.resolve(it).createNewFile()
        }

        stageTwo()
    }

    abstract fun stageTwo()
}