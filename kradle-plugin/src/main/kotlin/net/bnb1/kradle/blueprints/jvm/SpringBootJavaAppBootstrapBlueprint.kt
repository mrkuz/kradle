package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.support.tasks.BootstrapSpringBootJavaAppTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class SpringBootJavaAppBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties
    lateinit var extendsTask: String

    override fun doCreateTasks() {
        project.createHelperTask<BootstrapSpringBootJavaAppTask>(
            "bootstrapJavaApp",
            "Bootstrap Spring Boot (Java) project"
        ).also {
            project.tasks.getByName(extendsTask).dependsOn(it)
        }
    }

    override fun doConfigure() {
        val mainClass = applicationProperties.mainClass
        project.tasks.withType<BootstrapSpringBootJavaAppTask> {
            this.mainClass.set(mainClass)
        }
    }
}
