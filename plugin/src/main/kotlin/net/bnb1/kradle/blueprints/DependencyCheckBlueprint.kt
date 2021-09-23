package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.create
import org.gradle.api.Project
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import org.owasp.dependencycheck.gradle.tasks.Analyze

object DependencyCheckBlueprint : PluginBlueprint<DependencyCheckPlugin> {

    override fun configureEager(project: Project) {
        project.create<Analyze>("analyzeDependencies", "Analyzes dependencies for vulnerabilities") {
            config = DependencyCheckExtension(project).apply {
                scanConfigurations.add("compileClasspath")
                scanConfigurations.add("runtimeClasspath")
            }
        }
    }
}
