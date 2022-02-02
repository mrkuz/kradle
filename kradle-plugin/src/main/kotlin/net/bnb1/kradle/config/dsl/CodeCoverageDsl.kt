package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.BlueprintDsl

class CodeCoverageDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val jacoco = BlueprintDsl(blueprints.jacoco, properties.jacoco)
    val kover = BlueprintDsl(blueprints.kover, properties.kover)
}
