package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.alias
import net.bnb1.kradle.apply
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

class KtlintBlueprint(project: Project) : Blueprint(project) {

    override fun applyPlugins() {
        project.apply(KtlintPlugin::class.java)
    }

    override fun addAliases() {
        project.alias("lint", "Runs ktlint", "ktlintCheck")
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<KotlinLintProperties>()
        project.configure<KtlintExtension> {
            enableExperimentalRules.set(true)
            disabledRules.set(setOf("no-wildcard-imports"))
            version.set(properties.ktlintVersion.get())
        }
    }
}