package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.testImplementation
import net.bnb1.kradle.testRuntimeOnly
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class JunitJupiterBlueprint(project: Project) : Blueprint(project) {

    lateinit var junitJupiterProperties: JunitJupiterProperties

    override fun doAddDependencies() {
        project.dependencies {
            testImplementation("${Catalog.Dependencies.Test.junitApi}:${junitJupiterProperties.version}")
            testRuntimeOnly("${Catalog.Dependencies.Test.junitEngine}:${junitJupiterProperties.version}")
        }
    }

    override fun doConfigure() {
        project.tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}
