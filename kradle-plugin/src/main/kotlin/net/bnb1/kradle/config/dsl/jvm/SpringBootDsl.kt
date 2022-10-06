package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.SpringBootProperties
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.Value

class SpringBootDsl(properties: SpringBootProperties) {

    val version = Value(properties.version) { properties.version = it }
    val withDevTools = Optional("default") { properties.withDevTools = it }
    val useWebMvc = Optional("default") { properties.useWebMvc = it }
    val useWebFlux = Optional("default") { properties.useWebFlux = it }
    val useActuator = Optional("default") { properties.useActuator = it }
}
