package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.named

class SpringBootDevelopmentModeBlueprint(project: Project) : Blueprint(project) {

    lateinit var springBootProperties: SpringBootProperties

    lateinit var withBuildProfiles: () -> Boolean
    lateinit var extendsTask: String

    override fun doConfigure() {
        springBootProperties.withDevTools?.let {
            project.tasks.named<JavaExec>(extendsTask).configure {
                environment("KRADLE_AGENT_MODE", "rebuild")

                if (withBuildProfiles()) {
                    environment("SPRING_PROFILES_ACTIVE", project.extra["profile"].toString())
                }
            }
        }
    }
}
