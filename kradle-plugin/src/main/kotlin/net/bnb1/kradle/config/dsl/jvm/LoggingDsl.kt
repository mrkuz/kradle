package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.blueprints.jvm.LoggingProperties
import net.bnb1.kradle.dsl.Optional

class LoggingDsl(properties: LoggingProperties) {

    val withSlf4j = Optional(Catalog.Versions.slf4j) { properties.withSlf4j = it }
    val withLog4j = Optional(Catalog.Versions.log4j) { properties.withLog4j = it }
}
