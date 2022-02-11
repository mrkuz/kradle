package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class JmhDsl(properties: AllProperties) {

    var version = Value(properties.jmh.version) { properties.jmh.version = it }
}
