package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.*
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.named

class ApplicationBlueprint(project: Project) : Blueprint(project) {

    @Suppress("PrivatePropertyName")
    private val GROUP_PATTERN = Regex("^[a-z]+(\\.[a-z0-9]+)+$")

    override fun applyPlugins() {
        project.apply(ApplicationPlugin::class.java)
    }

    override fun checkPreconditions() {
        if (project.featureRegistry.get<LibraryFeature>().isEnabled()) {
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

    override fun createTasks() {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val agentResource = javaClass.getResource("/agent.jar")
        project.create<JavaExec>(
            "dev", "Runs the application and stops it when sources change (use with -t)",
        ) {
            val agentFile = project.rootDir.resolve(".gradle/kradle/agent.jar")
            doFirst {
                agentFile.parentFile.mkdirs()
                agentFile.writeBytes(agentResource.readBytes())
            }
            // Allows the application to figure out we are running in development mode
            environment("DEV_MODE", "true")
            // Tell agent about the project root
            environment("PROJECT_ROOT", project.rootDir)
            // Speed up start when developing
            jvmArgs = listOf("-XX:TieredStopAtLevel=1")
            classpath = mainSourceSet.runtimeClasspath
            jvmArgs = jvmArgs + listOf("-javaagent:${agentFile.absolutePath}")
        }
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<ApplicationProperties>()
        val mainClass = properties.mainClass
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

        project.tasks.named<JavaExec>("dev").configure { this.mainClass.set(mainClass.get()) }
    }
}
