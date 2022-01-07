package net.bnb1.kradle.plugins

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.tasks.KradleDumpTask
import net.bnb1.kradle.tracer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories

class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val context = KradleContext()
        project.extensions.create<KradleExtension>("kradle", context, project)

        val task = project.createHelperTask<KradleDumpTask>(
            "kradleDump",
            "Dumps kradle diagnostic information"
        )
        task.also { it.context = context }
        project.afterEvaluate { project.tracer.deactivate() }

        project.repositories {
            mavenCentral()
            google()
            gradlePluginPortal()
            mavenLocal()
        }
    }
}
