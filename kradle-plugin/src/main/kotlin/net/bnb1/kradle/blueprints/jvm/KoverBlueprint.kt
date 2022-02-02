package net.bnb1.kradle.blueprints.jvm

import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.KoverTaskExtension
import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

class KoverBlueprint(project: Project) : Blueprint(project) {

    lateinit var koverProperties: KoverProperties

    override fun doApplyPlugins() {
        project.apply(KoverPlugin::class.java)
    }

    override fun doConfigure() {
        project.tasks.withType<Test> {
            val extension = extensions.getByType(KoverTaskExtension::class.java)
            val enabled = name == "test" || koverProperties.includes.get().contains(name)
            extension.isDisabled = !enabled
            if (enabled) {
                finalizedBy("koverHtmlReport")
            }
        }
    }
}
