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
        val useKotest = kotlinTestProperties.useKotest
        if (useKotest.hasValue) {
            project.dependencies {
                testImplementation("${Catalog.Dependencies.Test.kotestAssertions}:${useKotest.get()}")
                if (withJunitJupiter()) {
                    testImplementation("${Catalog.Dependencies.Test.kotestJunit5}:${useKotest.get()}")
                }
            }
        }

        val useMockk = kotlinTestProperties.useMockk
        if (useMockk.hasValue) {
            project.dependencies {
                testImplementation("${Catalog.Dependencies.Test.mockk}:${useMockk.get()}")
            }
        }
    }
}
