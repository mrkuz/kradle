package net.bnb1.kradle.v1

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.apply
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.tasks.KradleDumpTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleCompatAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleCompatBasePlugin::class.java)

        val context = KradleContext()
        KradleCompat(context, project, KradleCompat.ProjectType.APPLICATION).activate()

        val task = project.createHelperTask<KradleDumpTask>(
            "kradleDump",
            "Dumps kradle diagnostic information"
        )
        task.also { it.context = context }

        project.logger.warn(
            "WARNING: The plugin 'net.bitsandbobs.kradle-app' is deprecated, " +
                "consider using 'net.bitsandbobs.kradle' instead."
        )
    }
}
