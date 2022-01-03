package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDsl
import org.gradle.api.Project

class KotlinLintProperties(project: Project) : Properties(project) {

    private val ktlintProperties = KtlintProperties(project)
    val ktlint = PropertiesDsl.Builder<KtlintProperties>(project)
        .properties(ktlintProperties)
        .build()
    val ktlintVersion = ktlintProperties.version
}
