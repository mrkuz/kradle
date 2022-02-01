package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.BlueprintDsl

class PackagingDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val uberJar = BlueprintDsl(blueprints.shadow, properties.uberJar)
}
