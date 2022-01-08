package net.bnb1.kradle.v1

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.KradleExtensionBase
import net.bnb1.kradle.apply
import net.bnb1.kradle.features.AllBlueprints
import net.bnb1.kradle.features.AllFeatures
import net.bnb1.kradle.features.AllProperties
import net.bnb1.kradle.features.FeaturePlan
import net.bnb1.kradle.inject
import net.bnb1.kradle.support.Tracer
import net.bnb1.kradle.tasks.KradleDumpTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class KradleCompatAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.logger.warn(
            "WARNING: The plugin 'net.bitsandbobs.kradle-app' is deprecated, " +
                "consider using 'net.bitsandbobs.kradle' instead."
        )

        project.apply(KradleCompatBasePlugin::class.java)

        val context = KradleContext()
        val tracer = Tracer()
        val properties = AllProperties(context)
        val blueprints = AllBlueprints(context, properties, project)
        val features = AllFeatures(context)
        FeaturePlan(features, blueprints).initialize()

        project.tasks.withType<KradleDumpTask> {
            inject {
                this.tracer = tracer
                this.context = context
            }
        }

        val extension = KradleExtensionBase(context, tracer, features, properties, project)
        KradleCompat(context, tracer, extension, project, KradleCompat.ProjectType.APPLICATION).activate()
    }
}
