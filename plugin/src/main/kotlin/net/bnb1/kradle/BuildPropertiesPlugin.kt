package net.bnb1.kradle

import net.bnb1.kradle.tasks.GenerateBuildPropertiesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildPropertiesPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.create("generateBuildProperties", "Generates build.properties", GenerateBuildPropertiesTask::class.java)
        project.tasks.named("processResources").configure {
            finalizedBy("generateBuildProperties")
        }
    }
}