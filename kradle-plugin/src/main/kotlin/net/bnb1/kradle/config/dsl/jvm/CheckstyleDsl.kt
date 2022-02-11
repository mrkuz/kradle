package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class CheckstyleDsl(properties: AllProperties) {

    val version = Value(properties.checkstyle.version) { properties.checkstyle.version = it }
    val configFile = Value(properties.checkstyle.configFile) { properties.checkstyle.configFile = it }
}
