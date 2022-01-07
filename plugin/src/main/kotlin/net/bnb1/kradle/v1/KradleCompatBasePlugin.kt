package net.bnb1.kradle.v1

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories

class KradleCompatBasePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create<KradleCompatExtension>("kradle")
        
        project.repositories {
            mavenCentral()
            google()
            gradlePluginPortal()
            mavenLocal()
        }
    }
}
