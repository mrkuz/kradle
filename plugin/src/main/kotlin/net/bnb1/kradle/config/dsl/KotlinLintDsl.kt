package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.BlueprintDsl

class KotlinLintDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val ktlint = BlueprintDsl(blueprints.ktlint, properties.ktlint)
    val ktlintVersion = properties.ktlint.version
}
