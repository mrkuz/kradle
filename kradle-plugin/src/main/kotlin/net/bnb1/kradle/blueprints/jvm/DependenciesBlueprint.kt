package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DependenciesBlueprint(project: Project) : Blueprint(project) {

    lateinit var dependenciesProperties: DependenciesProperties

    override fun doAddDependencies() {
        val useGuava = dependenciesProperties.useGuava
        useGuava.get {
            project.dependencies {
                implementation("${Catalog.Dependencies.guava}:$it")
            }
        }

        dependenciesProperties.useCaffeine.get {
            project.dependencies {
                implementation("${Catalog.Dependencies.caffeine}:$it}")
            }
            if (useGuava.hasValue) {
                project.dependencies {
                    implementation("${Catalog.Dependencies.caffeineGuava}:$it}")
                }
            }
        }

        dependenciesProperties.useLog4j.get {
            project.dependencies {
                implementation("${Catalog.Dependencies.log4j}:$it}")
            }
        }
    }
}
