package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import net.bnb1.kradle.support.tasks.GenerateBuildPropertiesTask
import org.gradle.api.Project

class BuildPropertiesBlueprint(project: Project) : Blueprint(project) {

    override fun doCreateTasks() {
        project.createTask<GenerateBuildPropertiesTask>("generateBuildProperties", "Generates build.properties")
    }

    override fun doConfigure() {
        project.tasks.named("processResources").configure {
            finalizedBy("generateBuildProperties")
        }
    }
}
