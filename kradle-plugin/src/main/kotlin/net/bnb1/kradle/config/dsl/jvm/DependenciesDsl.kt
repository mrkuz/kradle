package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Optional

class DependenciesDsl(properties: AllProperties) {

    val useGuava = Optional(Catalog.Versions.guava) { properties.dependencies.useGuava = it }
    val useCaffeine = Optional(Catalog.Versions.caffeine) { properties.dependencies.useCaffeine = it }
    val useLog4j = Optional(Catalog.Versions.log4j) { properties.logging.withLog4j = it }
}
