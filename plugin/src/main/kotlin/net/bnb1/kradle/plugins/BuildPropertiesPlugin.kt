package net.bnb1.kradle.plugins

import net.bnb1.kradle.create
import net.bnb1.kradle.tasks.GenerateBuildPropertiesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildPropertiesPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.create("generateBuildProperties", "Generates build.properties", GenerateBuildPropertiesTask::class.java)
    }
}