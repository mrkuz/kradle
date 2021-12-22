package net.bnb1.kradle.plugins

import net.bnb1.kradle.create
import net.bnb1.kradle.tasks.BootstrapLibTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BootstrapLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.create("bootstrap", "Bootstrap lib project", BootstrapLibTask::class.java)
    }
}
