package net.bnb1.kradle.plugins

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.config.KradleContext
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.inject
import net.bnb1.kradle.support.Tracer
import net.bnb1.kradle.tasks.KradleDumpTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories

class KradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.repositories {
            mavenCentral()
            google()
            gradlePluginPortal()
            mavenLocal()
        }

        val tracer = Tracer()
        project.afterEvaluate { tracer.deactivate() }

        val context = KradleContext(project).also { it.initialize() }

        val task = project.createHelperTask<KradleDumpTask>(
            "kradleDump",
            "Dumps kradle diagnostic information"
        )
        task.inject {
            this.properties = context.propertiesAsList()
            this.features = context.featuresAsList()
            this.tracer = tracer
        }

        project.extensions.create<KradleExtension>(
            "kradle",
            tracer,
            context.featureSets,
            context.features,
            context.properties,
            context.presets
        )
    }
}
