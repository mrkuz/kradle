package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.named

class PackageApplicationBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties

    override fun doConfigure() {
        val mainClass = applicationProperties.mainClass
        project.tasks.named<Jar>("jar").configure {
            manifest {
                attributes(Pair("Main-Class", mainClass.get()))
            }
        }
    }
}
