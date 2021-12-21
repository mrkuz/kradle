package net.bnb1.kradle.v1

import net.bnb1.kradle.apply
import org.gradle.api.Plugin
import org.gradle.api.Project

class KradleCompatLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.apply(KradleCompatBasePlugin::class.java)
        KradleCompat(project, KradleCompat.ProjectType.LIBRARY).activate()
    }
}