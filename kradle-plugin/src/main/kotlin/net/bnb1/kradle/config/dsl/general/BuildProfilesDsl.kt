package net.bnb1.kradle.config.dsl.general

import net.bnb1.kradle.blueprints.general.BuildProfilesProperties
import net.bnb1.kradle.dsl.Value

class BuildProfilesDsl(properties: BuildProfilesProperties) {

    val active = Value(properties.active) { properties.active = it }
}
