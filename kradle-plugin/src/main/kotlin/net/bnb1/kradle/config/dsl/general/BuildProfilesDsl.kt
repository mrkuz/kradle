package net.bnb1.kradle.config.dsl.general

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class BuildProfilesDsl(properties: AllProperties) {

    val active = Value(properties.buildProfiles.active) { properties.buildProfiles.active = it }
}
