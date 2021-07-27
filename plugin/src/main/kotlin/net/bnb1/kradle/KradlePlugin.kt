package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.rootProject.pluginManager.apply(KradleBasePlugin::class.java)

        project.tasks.create("helloWorld", HelloWorldTask::class.java).apply {
            group = Constants.TASK_GROUP
            description = "Display hello world"
        }
    }
}
