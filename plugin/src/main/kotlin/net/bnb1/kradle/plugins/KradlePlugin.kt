package net.bnb1.kradle.plugins

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.AllBlueprints
import net.bnb1.kradle.features.AllFeatures
import net.bnb1.kradle.features.AllProperties
import net.bnb1.kradle.features.FeaturePlan
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

        val context = KradleContext()
        val tracer = Tracer()
        project.afterEvaluate { tracer.deactivate() }

        val properties = AllProperties(context)
        val blueprints = AllBlueprints(context, properties, project)
        val features = AllFeatures(context)
        FeaturePlan(features, blueprints).initialize()

        val task = project.createHelperTask<KradleDumpTask>(
            "kradleDump",
            "Dumps kradle diagnostic information"
        )
        task.inject {
            this.context = context
            this.tracer = tracer
        }

        project.extensions.create<KradleExtension>("kradle", context, tracer, features, properties, project)
    }
}
