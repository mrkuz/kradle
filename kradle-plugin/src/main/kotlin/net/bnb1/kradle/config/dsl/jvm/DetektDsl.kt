package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.DetektProperties
import net.bnb1.kradle.dsl.Value

class DetektDsl(properties: DetektProperties) {

    val version = Value(properties.version) { properties.version = it }
    val configFile = Value(properties.configFile) { properties.configFile = it }
}
