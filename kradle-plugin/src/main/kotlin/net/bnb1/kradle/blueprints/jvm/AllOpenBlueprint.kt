package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

class AllOpenBlueprint(project: Project) : Blueprint(project) {

    override fun doApplyPlugins() {
        project.apply(AllOpenGradleSubplugin::class.java)
    }
}
