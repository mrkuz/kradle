package net.bnb1.kradle.v1

import net.bnb1.kradle.apply
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleCompatAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleCompatBasePlugin::class.java)
        KradleCompat(project, KradleCompat.ProjectType.APPLICATION).activate()

        project.logger.warn(
            "WARNING: The plugin 'net.bitsandbobs.kradle-app' is deprecated, " +
                "consider using 'net.bitsandbobs.kradle' instead."
        )
    }
}
