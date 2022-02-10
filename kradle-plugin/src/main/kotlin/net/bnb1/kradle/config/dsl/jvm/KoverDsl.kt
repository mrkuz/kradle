package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.ValueSet

class KoverDsl(properties: AllProperties) {

    val excludes = ValueSet(properties.kover.excludes)
}
