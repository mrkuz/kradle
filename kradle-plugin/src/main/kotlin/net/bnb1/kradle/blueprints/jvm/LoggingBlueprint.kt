package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import net.bnb1.kradle.implementation
import net.bnb1.kradle.support.tasks.GenerateLog4jConfigTask
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

class LoggingBlueprint(project: Project) : Blueprint(project) {

    lateinit var loggingProperties: LoggingProperties
    lateinit var extendsBootstrapTask: String

    override fun doCreateTasks() {
        loggingProperties.withLog4j?.let {
            val generateTask = project.createTask<GenerateLog4jConfigTask>(
                "generateLog4jConfig",
                "Generates log4j.xml"
            )
            project.tasks.findByName(extendsBootstrapTask)?.dependsOn(generateTask)
        }
    }

    override fun doAddDependencies() {
        loggingProperties.withSlf4j?.let {
            project.dependencies {
                implementation("${Catalog.Dependencies.slf4jApi}:$it")
                if (loggingProperties.withLog4j == null) {
                    implementation("${Catalog.Dependencies.slf4jSimple}:$it")
                }
            }
        }

        loggingProperties.withLog4j?.let {
            project.dependencies {
                implementation("${Catalog.Dependencies.log4jApi}:$it")
                implementation("${Catalog.Dependencies.log4jCore}:$it")
                if (loggingProperties.withSlf4j != null) {
                    implementation("${Catalog.Dependencies.log4jSlf4j}:$it")
                }
            }
        }
    }

    override fun doConfigure() {
        loggingProperties.withLog4j?.let {
            project.tasks.named<Jar>("jar").configure {
                manifest {
                    attributes(Pair("Multi-Release", "true"))
                }
            }
        }
    }
}
