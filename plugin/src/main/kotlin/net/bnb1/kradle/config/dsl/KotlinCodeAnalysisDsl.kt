package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.BlueprintDsl

class KotlinCodeAnalysisDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val detekt = BlueprintDsl(blueprints.detekt, properties.detekt)
    val detektConfigFile = properties.detekt.configFile
    val detektVersion = properties.detekt.version
}
