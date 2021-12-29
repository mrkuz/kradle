package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.create
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import org.owasp.dependencycheck.gradle.tasks.Analyze

class OwaspDependencyCheckBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(DependencyCheckPlugin::class.java)
    }

    override fun createTasks() {
        project.create<Analyze>("analyzeDependencies", "Analyzes dependencies for vulnerabilities") {
            config = DependencyCheckExtension(project).apply {
                scanConfigurations.add("compileClasspath")
                scanConfigurations.add("runtimeClasspath")
            }
        }
    }
}
