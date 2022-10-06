package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.support.tasks.BootstrapSpringBootKotlinAppTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

class SpringBootKotlinAppBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var applicationProperties: ApplicationProperties
    lateinit var extendsTask: String

    override fun doCreateTasks() {
        project.createHelperTask<BootstrapSpringBootKotlinAppTask>(
            "bootstrapKotlinApp",
            "Bootstrap Spring Boot (Kotlin) project"
        ).also {
            project.tasks.getByName(extendsTask).dependsOn(it)
        }
    }

    override fun doConfigure() {
        val mainClass = applicationProperties.mainClass
        project.tasks.withType<BootstrapSpringBootKotlinAppTask> {
            this.mainClass.set(mainClass)
        }
    }
}
