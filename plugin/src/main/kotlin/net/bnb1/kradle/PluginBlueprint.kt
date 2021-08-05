package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project

interface PluginBlueprint<out T : Plugin<Project>> {

    fun configureEager(project: Project) {}

    fun configure(project: Project, extension: KradleExtension) {}
}