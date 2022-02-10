package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import net.bnb1.kradle.sourceSets
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.named

class DevelopmentModeBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties
    lateinit var javaProperties: JavaProperties

    override fun doCreateTasks() {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val agentResource = javaClass.getResource("/kradle-agent.jar")

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        val toolchainService = project.extensions.getByType(JavaToolchainService::class.java)
        val launcher = toolchainService.launcherFor(javaExtension.toolchain)

        project.createTask<JavaExec>(
            "dev",
            "Runs the application and stops it when sources change (use with -t)"
        ) {
            val agentFile = project.rootDir.resolve(".gradle/kradle/kradle-agent.jar")
            doFirst {
                agentFile.parentFile.mkdirs()
                agentFile.writeBytes(agentResource.readBytes())
            }
            // Allows the application to figure out we are running in development mode
            environment("KRADLE_DEV_MODE", "true")
            // Tell agent about the project root
            environment("KRADLE_PROJECT_ROOT_DIR", project.rootDir)
            // Speed up start when developing
            jvmArgs = listOf("-XX:TieredStopAtLevel=1")
            if (javaProperties.previewFeatures) {
                jvmArgs = jvmArgs + "--enable-preview"
            }
            classpath = mainSourceSet.runtimeClasspath
            jvmArgs = jvmArgs + "-javaagent:${agentFile.absolutePath}"

            javaLauncher.set(launcher)
        }
    }

    override fun doConfigure() {
        val mainClass = applicationProperties.mainClass
        project.tasks.named<JavaExec>("dev").configure { this.mainClass.set(mainClass) }
    }
}
