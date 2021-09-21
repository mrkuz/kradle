package net.bnb1.kradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class GenerateBuildPropertiesTask : DefaultTask() {

    init {
        // Ensure that this task is always executed
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun run() {
        val output = project.buildDir.resolve("resources/main/build.properties")
        output.parentFile.mkdirs()
        output.printWriter().use {
            it.println("project.name=${project.name}")
            it.println("project.group=${project.group}")
            it.println("project.version=${project.properties["version"]}")
            it.println("build.timestamp=${System.currentTimeMillis() / 1000}")
            if (project.hasProperty("gitCommit")) {
                it.println("git.commit-id=${project.properties["gitCommit"]}")
            }
        }
    }
}