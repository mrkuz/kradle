package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class DetektDsl(properties: AllProperties) {

    val version = Value(Catalog.Versions.detekt) { properties.detekt.version = it }
    val configFile = Value("detekt-config.yml") { properties.detekt.configFile = it }
}
