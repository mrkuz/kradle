package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.withType

private val GROUP_PATTERN = Regex("^[a-z]+(\\.[a-z0-9]+)+$")

class ApplicationBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties
    lateinit var javaProperties: JavaProperties

    override fun doCheckPreconditions() {
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

    override fun doApplyPlugins() {
        project.apply(ApplicationPlugin::class.java)
    }

    override fun doConfigure() {
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

        if (javaProperties.previewFeatures.get()) {
            project.tasks.withType<JavaExec> {
                jvmArgs = jvmArgs + "--enable-preview"
            }
        }
    }
}
