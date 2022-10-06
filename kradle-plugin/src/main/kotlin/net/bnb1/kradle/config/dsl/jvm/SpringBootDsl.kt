package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.SpringBootProperties
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.Value

class SpringBootDsl(properties: SpringBootProperties) {

    val version = Value(properties.version) { properties.version = it }
    val withDevTools = Optional(properties.version) { properties.withDevTools = it }
    val useWeb = Optional(properties.version) { properties.useWeb = it }
    val useWebFlux = Optional(properties.version) { properties.useWebFlux = it }
    val useActuator = Optional(properties.version) { properties.useActuator = it }
}
