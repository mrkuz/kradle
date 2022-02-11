package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class JunitJupiterDsl(properties: AllProperties) {

    val version = Value(properties.junitJupiter.version) { properties.junitJupiter.version = it }
}
