package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class BenchmarkDsl(properties: AllProperties) {

    val jmh = Configurable(properties.jmh)
    val jmhVersion = properties.jmh.version
}
