package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.JavaExec
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.named

object ApplicationBlueprint : PluginBlueprint<ApplicationPlugin> {

    private val GROUP_PATTERN = Regex("^[a-z]+(\\.[a-z0-9]+)+$")

    override fun configure(project: Project, extension: KradleExtension) {
        if (!GROUP_PATTERN.matches(project.group.toString())) {
            project.logger.warn("WARNING: Group doesn't comply with Java's package name rules: ${project.group}")
        }
        if (project.version == "unspecified") {
            project.logger.warn("WARNING: Version is not specified")
        }

        project.tasks.named<JavaExec>("run").configure {
            // Allows the application to figure out we are running in development mode
            environment("DEV_MODE", "true")
            environment("BNB1_PROFILE", "dev")
            // Speed up start when developing
            jvmArgs = listOf("-XX:TieredStopAtLevel=1")
        }

        val javaExtension = project.extensions.getByType(JavaApplication::class.java)
        val mainClass = javaExtension.mainClass.get()
        project.tasks.named<Jar>("jar").configure {
            manifest {
                attributes(Pair("Main-Class", mainClass))
            }
        }
    }
}