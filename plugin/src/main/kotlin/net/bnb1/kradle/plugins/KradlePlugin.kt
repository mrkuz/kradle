package net.bnb1.kradle.plugins

import net.bnb1.kradle.KradleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories
import javax.inject.Inject

class KradlePlugin @Inject constructor(private val factory: ObjectFactory) : Plugin<Project> {

    override fun apply(project: Project) {

        project.extensions.create<KradleExtension>("kradle")

        project.repositories {
            mavenCentral()
            gradlePluginPortal()
            mavenLocal()
        }
    }
}
