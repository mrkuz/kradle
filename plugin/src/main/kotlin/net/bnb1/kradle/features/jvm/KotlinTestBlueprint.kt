package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinTestBlueprint(project: Project) : Blueprint(project) {

    override fun addDependencies() {
        val properties = project.propertiesRegistry.get<KotlinTestProperties>()
        val withJunitJupiter = project.propertiesRegistry.get<TestProperties>().junitJupiterVersion.hasValue
        if (properties.kotestVersion.hasValue) {
            project.dependencies {
                testImplementation("${Catalog.Dependencies.Test.kotestAssertions}:${properties.kotestVersion.get()}")
                if (withJunitJupiter) {
                    testImplementation("${Catalog.Dependencies.Test.kotestJunit5}:${properties.kotestVersion.get()}")
                } else {
                    testImplementation("${Catalog.Dependencies.Test.kotestJunit4}:${properties.kotestVersion.get()}")
                }
            }
        }
        if (properties.mockkVersion.hasValue) {
            project.dependencies {
                testImplementation("${Catalog.Dependencies.Test.mockk}:${properties.mockkVersion.get()}")
            }
        }
    }
}
