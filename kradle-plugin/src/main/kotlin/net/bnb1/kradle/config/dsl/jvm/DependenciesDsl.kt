package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.blueprints.jvm.DependenciesProperties
import net.bnb1.kradle.dsl.Optional

class DependenciesDsl(properties: DependenciesProperties) {

    val useGuava = Optional(Catalog.Versions.guava) { properties.useGuava = it }
    val useCaffeine = Optional(Catalog.Versions.caffeine) { properties.useCaffeine = it }
    val useLog4j = Optional(Catalog.Versions.log4j) { properties.useLog4j = it }
}
