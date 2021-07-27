package net.bnb1.kradle

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import java.lang.RuntimeException

class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val task = project.tasks.create("helloWorld", HelloWorldTask::class.java)
        task.group = "Kradle"
        task.description = "Print hello world"
    }
}
