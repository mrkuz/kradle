package net.bnb1.kradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.util.*

class ProjectPropertiesPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val propertiesFile = project.rootDir.resolve("project.properties")
        if (propertiesFile.exists()) {
            val properties = Properties()
            propertiesFile.inputStream().use { properties.load(it) }
            properties.forEach { (k, v) ->
                val key = k.toString()
                if (project.hasProperty(key)) {
                    project.logger.warn("WARNING: Property '$key' already exists. Skipping.")
                } else {
                    project.extra[key] = v.toString()
                }
            }
        }
    }
}
