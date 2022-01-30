package net.bnb1.kradle.v1

import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.tasks.KradleDumpTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories

class KradleCompatBasePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.repositories {
            mavenCentral()
            google()
            gradlePluginPortal()
            mavenLocal()
        }

        project.createHelperTask<KradleDumpTask>(
            "kradleDump",
            "Dumps kradle diagnostic information"
        )

        project.extensions.create<KradleCompatExtension>("kradle")
    }
}
