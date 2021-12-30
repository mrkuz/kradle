package net.bnb1.kradle.plugins

import net.bnb1.kradle.createTask
import net.bnb1.kradle.tasks.BootstrapLibTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootstrapLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.createTask<BootstrapLibTask>("bootstrap", "Bootstrap lib project")
    }
}
