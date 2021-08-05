package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension

object DependencyCheckBlueprint : PluginBlueprint<DependencyCheckPlugin> {

    override fun configure(project: Project, extension: KradleExtension) {
        project.configure<DependencyCheckExtension> {
            scanConfigurations.add("compileClasspath")
            scanConfigurations.add("runtimeClasspath")
        }

        project.alias("analyzeDependencies", "Analyzes dependencies for vulnerabilities", "dependencyCheckAnalyze")
    }
}