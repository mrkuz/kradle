package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.alias
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project

class PackageBlueprint(project: Project) : Blueprint(project) {

    override fun addAliases() {
        project.alias("package", "Creates JAR", "jar")
    }
}
