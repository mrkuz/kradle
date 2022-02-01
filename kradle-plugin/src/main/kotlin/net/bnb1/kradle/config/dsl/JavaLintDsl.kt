package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.BlueprintDsl

class JavaLintDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val checkstyle = BlueprintDsl(blueprints.checkstyle, properties.checkstyle)
}
