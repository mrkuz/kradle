package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.KoverProperties
import net.bnb1.kradle.dsl.ValueSet

class KoverDsl(properties: KoverProperties) {

    val excludes = ValueSet(properties.excludes)
}
