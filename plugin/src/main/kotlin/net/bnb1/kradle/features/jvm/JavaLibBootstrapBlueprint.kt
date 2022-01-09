package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.features.general.BootstrapFeature
import net.bnb1.kradle.support.tasks.BootstrapJavaLibTask
import org.gradle.api.Project

class JavaLibBootstrapBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        project.createHelperTask<BootstrapJavaLibTask>(
            "bootstrapJavaLib",
            "Bootstrap Java library project"
        ).also {
            project.tasks.getByName(BootstrapFeature.MAIN_TASK).dependsOn(it)
        }
    }
}
