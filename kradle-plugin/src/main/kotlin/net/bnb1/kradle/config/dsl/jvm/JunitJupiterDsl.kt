package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.JunitJupiterProperties
import net.bnb1.kradle.dsl.Value

class JunitJupiterDsl(properties: JunitJupiterProperties) {

    val version = Value(properties.version) { properties.version = it }
}
