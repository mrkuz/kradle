package net.bnb1.kradle.v1

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.apply
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
        project.tasks.withType<KradleDumpTask> {
            inject {
                this.context = context
            }
        }

        val tracer = Tracer()
        context.register(tracer)

        KradleCompat(context, project, KradleCompat.ProjectType.APPLICATION).activate()
    }
}
