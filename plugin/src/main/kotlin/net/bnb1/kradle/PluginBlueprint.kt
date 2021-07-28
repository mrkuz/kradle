package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project

interface PluginBlueprint<T : Plugin<Project>> {

    fun configure(project: Project) {}
}