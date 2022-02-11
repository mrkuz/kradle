package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class DetektDsl(properties: AllProperties) {

    val version = Value(properties.detekt.version) { properties.detekt.version = it }
    val configFile = Value(properties.detekt.configFile) { properties.detekt.configFile = it }
}
