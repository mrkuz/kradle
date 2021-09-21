package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import net.bnb1.kradle.annotations.Invasive
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

@Invasive
object KtlintBlueprint : PluginBlueprint<KtlintPlugin> {

    override fun configureEager(project: Project) {
        project.alias("lint", "Runs ktlint", "ktlintCheck")
    }

    override fun configure(project: Project, extension: KradleExtension) {
        project.configure<KtlintExtension> {
            enableExperimentalRules.set(true)
            disabledRules.set(setOf("no-wildcard-imports"))
            version.set(extension.ktlintVersion.get())
        }
    }
}
