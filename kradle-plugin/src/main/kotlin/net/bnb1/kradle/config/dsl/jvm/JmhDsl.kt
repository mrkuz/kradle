package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class JmhDsl(properties: AllProperties) {

    var version = Value(Catalog.Versions.jmh) { properties.jmh.version = it }
}
