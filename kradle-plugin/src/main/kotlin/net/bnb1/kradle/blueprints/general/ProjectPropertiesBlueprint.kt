package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.util.*

class ProjectPropertiesBlueprint(project: Project) : Blueprint(project) {

    lateinit var withBuildProfiles: () -> Boolean

    override fun doAddExtraProperties() {
        val properties = Properties()
        val projectProperties = project.projectDir.resolve("project.properties")
        if (projectProperties.exists()) {
            projectProperties.inputStream().use { properties.load(it) }
        }
        if (withBuildProfiles()) {
            val profileProperties = project.projectDir.resolve("project-${project.extra["profile"]}.properties")
            if (profileProperties.exists()) {
                profileProperties.inputStream().use { properties.load(it) }
            }
        }

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
