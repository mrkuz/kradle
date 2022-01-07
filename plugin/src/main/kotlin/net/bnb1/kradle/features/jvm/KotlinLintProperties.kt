package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties

class KotlinLintProperties(context: KradleContext) : Properties() {

    private val _ktlintProperties by context { KtlintProperties() }
    val ktlint = PropertiesDsl(_ktlintProperties)
    val ktlintVersion = _ktlintProperties.version
}
