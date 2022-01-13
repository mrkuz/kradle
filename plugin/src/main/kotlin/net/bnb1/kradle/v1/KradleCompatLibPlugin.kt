package net.bnb1.kradle.v1

import net.bnb1.kradle.apply
import net.bnb1.kradle.config.KradleContext
import net.bnb1.kradle.config.dsl.KradleExtensionDsl
import net.bnb1.kradle.inject
import net.bnb1.kradle.support.Tracer
import net.bnb1.kradle.tasks.KradleDumpTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class KradleCompatLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.logger.warn(
            "WARNING: The plugin 'net.bitsandbobs.kradle-lib' is deprecated, " +
                "consider using 'net.bitsandbobs.kradle' instead."
        )

        project.apply(KradleCompatBasePlugin::class.java)

        val tracer = Tracer()
        val context = KradleContext(project).also { it.initialize() }

        project.tasks.withType<KradleDumpTask> {
            inject {
                this.properties = context.propertiesAsList()
                this.features = context.featuresAsList()
                this.tracer = tracer
            }
        }

        val extension = KradleExtensionDsl(tracer, context.featureSets, context.features, context.properties)
        KradleCompat(
            tracer,
            context.properties,
            context.blueprints,
            extension,
            project,
            KradleCompat.ProjectType.LIBRARY
        ).activate()
    }
}
