package net.bnb1.kradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class NoOpPlugin : Plugin<Project> {

    override fun apply(target: Project) = Unit
}
