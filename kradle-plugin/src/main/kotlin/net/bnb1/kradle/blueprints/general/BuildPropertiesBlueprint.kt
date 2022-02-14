package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import net.bnb1.kradle.support.tasks.GenerateBuildPropertiesTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

class BuildPropertiesBlueprint(project: Project) : Blueprint(project) {

    lateinit var withGit: () -> Boolean
    lateinit var withBuildProfiles: () -> Boolean

    override fun doCreateTasks() {
        project.createTask<GenerateBuildPropertiesTask>("generateBuildProperties", "Generates build.properties") {
            if (withGit()) {
                gitCommit.set(
                    project.provider {
                        if (project.extra.has("gitCommit")) {
                            project.extra["gitCommit"].toString()
                        } else {
                            ""
                        }
                    }
                )
            }
            if (withBuildProfiles()) {
                profile.set(project.provider { project.extra["profile"].toString() })
            }
        }
    }

    override fun doConfigure() {
        project.tasks.named("processResources").configure {
            finalizedBy("generateBuildProperties")
        }
    }
}
