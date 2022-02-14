package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.JmhProperties
import net.bnb1.kradle.dsl.Value

class JmhDsl(properties: JmhProperties) {

    var version = Value(properties.version) { properties.version = it }
}
