package net.bnb1.kradle

import net.bnb1.kradle.blueprints.DependencyUpdate
import net.bnb1.kradle.tasks.HelloWorldTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val factory = TaskFactory(project)
        factory.create("dependencyUpdates", "Display dependency updates", DependencyUpdate)
        factory.create("helloWorld", "Display hello world", HelloWorldTask::class.java)
    }
}
