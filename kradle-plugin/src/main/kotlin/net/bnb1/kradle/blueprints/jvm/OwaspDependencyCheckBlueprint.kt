package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import org.gradle.api.Project
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import org.owasp.dependencycheck.gradle.tasks.Analyze

class OwaspDependencyCheckBlueprint(project: Project) : Blueprint(project) {

    override fun doApplyPlugins() {
        project.apply(DependencyCheckPlugin::class.java)
    }

    override fun doCreateTasks() {
        project.createTask<Analyze>("analyzeDependencies", "Analyzes dependencies for vulnerabilities") {
            config = DependencyCheckExtension(project).apply {
                scanConfigurations.add("compileClasspath")
                scanConfigurations.add("runtimeClasspath")
            }
        }
    }
}
