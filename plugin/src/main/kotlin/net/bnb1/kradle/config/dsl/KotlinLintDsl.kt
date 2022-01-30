package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class KotlinLintDsl(properties: AllProperties) {

    val ktlint = Configurable(properties.ktlint)
    val ktlintVersion = properties.ktlint.version
}
