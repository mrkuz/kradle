package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project

class PmdBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        println("PMD")
    }
}
