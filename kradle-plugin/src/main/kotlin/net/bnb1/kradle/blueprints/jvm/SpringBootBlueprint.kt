package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project

class SpringBootBlueprint(project: Project) : Blueprint(project) {

    lateinit var springBootProperties: SpringBootProperties
}
