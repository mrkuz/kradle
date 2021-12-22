package net.bnb1.kradle.plugins

import net.bnb1.kradle.create
import net.bnb1.kradle.tasks.BootstrapAppTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootstrapAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.create("bootstrap", "Bootstrap app project", BootstrapAppTask::class.java)
    }
}
