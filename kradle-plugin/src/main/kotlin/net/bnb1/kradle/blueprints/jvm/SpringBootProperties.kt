package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class SpringBootProperties(var version: String) : Properties {

    var withDevTools: String? = null
    var useWebMvc: String? = null
    var useWebFlux: String? = null
    var useActuator: String? = null
}
