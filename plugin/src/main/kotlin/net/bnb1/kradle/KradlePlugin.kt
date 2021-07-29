package net.bnb1.kradle

import net.bnb1.kradle.blueprints.DependencyUpdatesBlueprint
import net.bnb1.kradle.blueprints.DokkaHtmlBlueprint
import net.bnb1.kradle.blueprints.KotlinBlueprint
import net.bnb1.kradle.tasks.HelloWorldTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories

class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.repositories {
            gradlePluginPortal()
            mavenCentral()
            mavenLocal()
        }

        project.apply(KotlinBlueprint)

        project.create("dependencyUpdates", "Displays dependency updates", DependencyUpdatesBlueprint)
        project.create("docs", "Generates HTML documentation", DokkaHtmlBlueprint)
        project.create("helloWorld", "Displays hello world", HelloWorldTask::class.java)
    }
}
