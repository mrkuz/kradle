package net.bnb1.kradle.blueprints

import net.bnb1.kradle.PluginBlueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

object KtlintBlueprint : PluginBlueprint<KtlintPlugin> {

    override fun configure(project: Project) {
        project.configure<KtlintExtension> {
            enableExperimentalRules.set(true)
            disabledRules.set(setOf("no-wildcard-imports"))
        }
    }
}