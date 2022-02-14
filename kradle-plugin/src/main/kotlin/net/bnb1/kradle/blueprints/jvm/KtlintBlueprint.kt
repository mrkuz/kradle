package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.apply
import net.bnb1.kradle.core.Blueprint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

class KtlintBlueprint(project: Project) : Blueprint(project) {

    lateinit var ktlintProperties: KtlintProperties
    lateinit var lintProperties: LintProperties
    lateinit var extendsTask: String

    override fun doApplyPlugins() {
        project.apply(KtlintPlugin::class.java)
    }

    override fun doConfigure() {
        project.configure<KtlintExtension> {
            enableExperimentalRules.set(true)
            disabledRules.set(ktlintProperties.ruleExcludes)
            version.set(ktlintProperties.version)
            ignoreFailures.set(lintProperties.ignoreFailures)
            reporters {
                reporter(ReporterType.HTML)
            }
        }

        project.tasks.getByName(extendsTask).dependsOn("ktlintCheck")
    }
}
