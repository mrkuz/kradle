package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class KotlinLintProperties(project: Project) : Properties() {

    private val ktlintProperties = KtlintProperties()
    val ktlint = PropertiesDsl.Builder<KtlintProperties>(project)
        .properties(ktlintProperties)
        .build()
    val ktlintVersion = ktlintProperties.version
}
