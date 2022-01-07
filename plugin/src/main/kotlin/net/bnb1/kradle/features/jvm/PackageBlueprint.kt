package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.alias
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.named

class PackageBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties

    override fun addAliases() {
        project.alias("package", "Creates JAR", "jar")
    }

    override fun configure() {
        val mainClass = applicationProperties.mainClass
        if (project.featureRegistry.get<ApplicationFeature>().isEnabled) {
            project.tasks.named<Jar>("jar").configure {
                manifest {
                    attributes(Pair("Main-Class", mainClass.get()))
                }
            }
        }
    }
}
