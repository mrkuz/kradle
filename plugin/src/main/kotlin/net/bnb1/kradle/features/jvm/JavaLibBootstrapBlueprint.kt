package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.support.tasks.BootstrapJavaLibTask
import org.gradle.api.Project

class JavaLibBootstrapBlueprint(project: Project) : Blueprint(project) {

    lateinit var extendsTask: String

    override fun doCreateTasks() {
        project.createHelperTask<BootstrapJavaLibTask>(
            "bootstrapJavaLib",
            "Bootstrap Java library project"
        ).also {
            project.tasks.getByName(extendsTask).dependsOn(it)
        }
    }
}
