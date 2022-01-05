package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.withType

private val GROUP_PATTERN = Regex("^[a-z]+(\\.[a-z0-9]+)+$")

class ApplicationBlueprint(project: Project) : Blueprint(project) {

    override fun checkPreconditions() {
        if (project.featureRegistry.get<LibraryFeature>().isEnabled) {
            throw GradleException("You can only enable 'application' or 'library' feature")
        }

        if (project.group.toString().isEmpty()) {
            project.logger.warn("WARNING: Group is not specified")
        }
        if (!GROUP_PATTERN.matches(project.group.toString())) {
            project.logger.warn("WARNING: Group doesn't comply with Java's package name rules")
        }
        if (project.version == "unspecified") {
            project.logger.warn("WARNING: Version is not specified")
        }
    }

    override fun applyPlugins() {
        project.apply(ApplicationPlugin::class.java)
    }

    override fun configure() {
        val applicationProperties = project.propertiesRegistry.get<ApplicationProperties>()
        val mainClass = applicationProperties.mainClass
        val javaExtension = project.extensions.getByType(JavaApplication::class.java)
        if (!mainClass.hasValue) {
            // Backward compatibility: Allow setting main class in application extension
            if (javaExtension.mainClass.isPresent) {
                mainClass.set(javaExtension.mainClass.get())
            } else {
                throw GradleException("Main class is not set")
            }
        } else {
            javaExtension.mainClass.set(mainClass.get())
        }

        val javaProperties = project.propertiesRegistry.get<JavaProperties>()
        if (javaProperties.previewFeatures.get()) {
            project.tasks.withType<JavaExec> {
                jvmArgs = jvmArgs + "--enable-preview"
            }
        }
    }
}
