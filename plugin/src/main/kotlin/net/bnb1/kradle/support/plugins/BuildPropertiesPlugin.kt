package net.bnb1.kradle.support.plugins

import net.bnb1.kradle.createTask
import net.bnb1.kradle.support.tasks.GenerateBuildPropertiesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildPropertiesPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.createTask<GenerateBuildPropertiesTask>("generateBuildProperties", "Generates build.properties")
    }
}
