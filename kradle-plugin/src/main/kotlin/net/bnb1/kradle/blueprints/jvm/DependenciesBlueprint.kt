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
        useGuava?.let {
            project.dependencies {
                implementation("${Catalog.Dependencies.guava}:$it")
            }
        }

        dependenciesProperties.useCaffeine?.let {
            project.dependencies {
                implementation("${Catalog.Dependencies.caffeine}:$it")
            }
            if (useGuava != null) {
                project.dependencies {
                    implementation("${Catalog.Dependencies.caffeineGuava}:$it")
                }
            }
        }

        dependenciesProperties.useLog4j?.let {
            project.dependencies {
                implementation("${Catalog.Dependencies.log4jApi}:$it")
                implementation("${Catalog.Dependencies.log4jCore}:$it")
            }
        }
    }
}
