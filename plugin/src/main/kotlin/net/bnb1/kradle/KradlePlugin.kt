package net.bnb1.kradle

import net.bnb1.kradle.blueprints.DependencyUpdatesBlueprint
import net.bnb1.kradle.blueprints.DokkaHtmlBlueprint
import net.bnb1.kradle.tasks.HelloWorldTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.closureOf

class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.repositories(closureOf<RepositoryHandler> {
            gradlePluginPortal()
            mavenCentral()
        })

        val factory = TaskFactory(project)
        factory.create("dependencyUpdates", "Displays dependency updates", DependencyUpdatesBlueprint)
        factory.create("docs", "Generates HTML documentation", DokkaHtmlBlueprint)
        factory.create("helloWorld", "Displays hello world", HelloWorldTask::class.java)
    }
}
