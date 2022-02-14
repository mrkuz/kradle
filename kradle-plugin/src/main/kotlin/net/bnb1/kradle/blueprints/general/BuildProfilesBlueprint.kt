package net.bnb1.kradle.blueprints.general

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.render
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

class BuildProfilesBlueprint(project: Project) : Blueprint(project) {

    lateinit var buildProfilesProperties: BuildProfilesProperties

    override fun doAddExtraProperties() {
        project.extra["profile"] = project.render(buildProfilesProperties.active)
    }
}
