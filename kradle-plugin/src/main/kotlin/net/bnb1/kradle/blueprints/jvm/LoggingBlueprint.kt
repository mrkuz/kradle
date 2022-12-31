package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class LoggingBlueprint(project: Project) : Blueprint(project) {

    lateinit var loggingProperties: LoggingProperties

    override fun doAddDependencies() {
        loggingProperties.withSlf4j?.let {
            project.dependencies {
                implementation("${Catalog.Dependencies.slf4jApi}:$it")
            }
        }

        loggingProperties.withLog4j?.let {
            project.dependencies {
                implementation("${Catalog.Dependencies.log4jApi}:$it")
                implementation("${Catalog.Dependencies.log4jCore}:$it")
            }
        }
    }
}
