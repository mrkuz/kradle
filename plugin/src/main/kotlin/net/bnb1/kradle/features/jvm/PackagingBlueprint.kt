package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.alias
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project

class PackagingBlueprint(project: Project) : Blueprint(project) {

    override fun doAddAliases() {
        project.alias("package", "Creates JAR", "jar")
    }
}
