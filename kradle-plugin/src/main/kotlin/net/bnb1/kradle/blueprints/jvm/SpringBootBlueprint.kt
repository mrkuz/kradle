package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.implementation
import net.bnb1.kradle.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SpringBootBlueprint(project: Project) : Blueprint(project) {

    lateinit var springBootProperties: SpringBootProperties

    lateinit var withDevelopmentMode: () -> Boolean
    lateinit var developmentConfiguration: String

    override fun doAddDependencies() {
        project.dependencies {
            implementation(platform("${Catalog.Dependencies.Platform.springBoot}:${springBootProperties.version}"))
            implementation("${Catalog.Dependencies.springBootStarter}:${springBootProperties.version}")
            testImplementation("${Catalog.Dependencies.Test.springBootStarterTest}:${springBootProperties.version}")
            springBootProperties.useWeb?.let {
                implementation(
                    "${Catalog.Dependencies.springBootStarterWeb}:$it"
                )
            }
            springBootProperties.useWebFlux?.let {
                implementation(
                    "${Catalog.Dependencies.springBootStarterWebFlux}:$it"
                )
            }
            springBootProperties.useActuator?.let {
                implementation(
                    "${Catalog.Dependencies.springBootStarterActuator}:$it"
                )
            }

            if (withDevelopmentMode()) {
                springBootProperties.withDevTools?.let {
                    add(
                        developmentConfiguration,
                        "${Catalog.Dependencies.Tools.springBootDevtools}:$it"
                    )
                }
            }
        }
    }
}
