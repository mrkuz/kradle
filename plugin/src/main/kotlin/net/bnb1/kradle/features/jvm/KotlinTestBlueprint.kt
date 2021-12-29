package net.bnb1.kradle.features.jvm

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
                testImplementation("io.kotest:kotest-assertions-core:${properties.kotestVersion.get()}")
                if (withJunitJupiter) {
                    testImplementation("io.kotest:kotest-runner-junit5:${properties.kotestVersion.get()}")
                } else {
                    testImplementation("io.kotest:kotest-runner-junit4:${properties.kotestVersion.get()}")
                }
            }
        }
        if (properties.mockkVersion.hasValue) {
            project.dependencies {
                testImplementation("io.mockk:mockk:${properties.mockkVersion.get()}")
            }
        }
    }
}
