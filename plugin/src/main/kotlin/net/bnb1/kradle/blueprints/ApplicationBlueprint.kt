package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.create
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.JavaExec
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.named

object ApplicationBlueprint : PluginBlueprint<ApplicationPlugin> {

    private val GROUP_PATTERN = Regex("^[a-z]+(\\.[a-z0-9]+)+$")

    @Suppress("DEPRECATION")
    override fun configure(project: Project, extension: KradleExtension) {
        if (project.group.toString().isEmpty()) {
            project.logger.warn("WARNING: Group is not specified")
        }
        if (!GROUP_PATTERN.matches(project.group.toString())) {
            project.logger.warn("WARNING: Group doesn't comply with Java's package name rules")
        }
        if (project.version == "unspecified") {
            project.logger.warn("WARNING: Version is not specified")
        }

        val javaExtension = project.extensions.getByType(JavaApplication::class.java)
        if (extension.mainClass.isEmpty()) {
            // Backward compatibility: Allow setting main class in application extension
            if (javaExtension.mainClass.isPresent) {
                extension.mainClass(javaExtension.mainClass.get())
            } else {
                throw GradleException("Main class is not set")
            }
        } else {
            javaExtension.mainClass.set(extension.mainClass)
        }

        project.tasks.named<JavaExec>("run").configure { configureExecTask(this) }
        project.tasks.named<Jar>("jar").configure {
            manifest {
                attributes(Pair("Main-Class", extension.mainClass))
            }
        }

        val javaConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        val mainSourceSet = javaConvention.sourceSets.getByName("main")
        val agentResource = javaClass.getResource("/agent.jar")
        project.create(
            "dev",
            "Runs the application and stops it when sources change (use with -t)",
            JavaExec::class.java
        ).apply {
            val agentFile = project.rootDir.resolve(".gradle/kradle/agent.jar")
            doFirst {
                agentFile.parentFile.mkdirs()
                agentFile.writeBytes(agentResource.readBytes())
            }
            configureExecTask(this)
            mainClass.set(extension.mainClass)
            classpath = mainSourceSet.runtimeClasspath
            jvmArgs = jvmArgs + listOf("-javaagent:${agentFile.absolutePath}")
        }
    }

    private fun configureExecTask(task: JavaExec) = task.apply {
        // Allows the application to figure out we are running in development mode
        environment("DEV_MODE", "true")
        environment("BNB1_PROFILE", "dev")
        environment("PROJECT_ROOT", project.rootDir)
        // Speed up start when developing
        jvmArgs = listOf("-XX:TieredStopAtLevel=1")
    }
}