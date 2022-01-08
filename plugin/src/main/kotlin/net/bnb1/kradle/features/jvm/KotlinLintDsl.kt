package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.features.AllProperties

class KotlinLintDsl(properties: AllProperties) {

    val ktlint = Configurable(properties.ktlint)
    val ktlintVersion = properties.ktlint.version
}
