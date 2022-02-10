package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value

class CheckstyleDsl(properties: AllProperties) {

    val version = Value(Catalog.Versions.checkstyle) { properties.checkstyle.version = it }
    val configFile = Value("checkstyle.xml") { properties.checkstyle.configFile = it }
}
