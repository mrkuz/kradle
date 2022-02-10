package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.dsl.Value

class BenchmarkDsl(properties: AllProperties) {

    val jmh = Configurable(JmhDsl(properties))
    val jmhVersion = Value(properties.jmh.version) { properties.jmh.version = it }
}
