package net.bnb1.kradle

import org.gradle.api.Plugin
import org.gradle.api.Project

interface PluginBlueprint<out T : Plugin<Project>> {

    /**
     * Implement for eager configuration. This method is called immediately after the plugin is applied.
     * Always called, even if the blueprint is disabled.
     *
     * Should be used to:
     * - Create tasks
     * - Create source sets
     * - Add alias for task
     */
    fun configureEager(project: Project) {}

    /**
     * Implement for late configuration, after the project is evaluated. This is not called if the blueprint is disabled.
     *
     * Should be used to:
     * - Add dependencies
     * - Behaviour allowed to be disabled
     */
    fun configure(project: Project, extension: KradleExtension) {}
}