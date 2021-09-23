package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.annotations.Invasive
import net.bnb1.kradle.create
import net.bnb1.kradle.sourceSets
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.named

@Invasive
object ApplicationBlueprint : PluginBlueprint<ApplicationPlugin> {

    private val GROUP_PATTERN = Regex("^[a-z]+(\\.[a-z0-9]+)+$")

    override fun configureEager(project: Project) {
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
            configureTask(this)
            classpath = mainSourceSet.runtimeClasspath
            jvmArgs = jvmArgs + listOf("-javaagent:${agentFile.absolutePath}")
        }
    }


    override fun configure(project: Project, extension: KradleExtension) {
        checkProjectProperties(project)

        val javaExtension = project.extensions.getByType(JavaApplication::class.java)
        if (extension.mainClass.isEmpty()) {
            // Backward compatibility: Allow setting main class in application extension
            if (javaExtension.mainClass.isPresent) {
                extension.mainClass(javaExtension.mainClass.get(), true)
            } else {
                throw GradleException("Main class is not set")
            }
        } else {
            javaExtension.mainClass.set(extension.mainClass)
        }

        project.tasks.named<JavaExec>("dev").configure { mainClass.set(extension.mainClass) }
        project.tasks.named<Jar>("jar").configure {
            manifest {
                attributes(Pair("Main-Class", extension.mainClass))
            }
        }
    }

    private fun configureTask(task: JavaExec) = task.apply {
        // Allows the application to figure out we are running in development mode
        environment("DEV_MODE", "true")
        environment("PROJECT_ROOT", project.rootDir)
        // Speed up start when developing
        jvmArgs = listOf("-XX:TieredStopAtLevel=1")
    }

    private fun checkProjectProperties(project: Project) {
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
}
