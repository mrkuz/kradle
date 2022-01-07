package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.features.Blueprint
import org.gradle.api.Project
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

class AllOpenBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(AllOpenGradleSubplugin::class.java)
    }
}
