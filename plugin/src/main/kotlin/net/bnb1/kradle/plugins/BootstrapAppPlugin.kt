package net.bnb1.kradle.plugins

import net.bnb1.kradle.createTask
import net.bnb1.kradle.tasks.BootstrapAppTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootstrapAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.createTask<BootstrapAppTask>("bootstrap", "Bootstrap app project")
    }
}
