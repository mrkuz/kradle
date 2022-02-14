package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinTestBlueprint(project: Project) : Blueprint(project) {

    lateinit var kotlinTestProperties: KotlinTestProperties
    lateinit var withJunitJupiter: () -> Boolean

    override fun doAddDependencies() {
        kotlinTestProperties.useKotest?.let {
            project.dependencies {
                testImplementation("${Catalog.Dependencies.Test.kotestAssertions}:$it")
                if (withJunitJupiter()) {
                    testImplementation("${Catalog.Dependencies.Test.kotestJunit5}:$it")
                }
            }
        }

        kotlinTestProperties.useMockk?.let {
            project.dependencies {
                testImplementation("${Catalog.Dependencies.Test.mockk}:$it")
            }
        }
    }
}
