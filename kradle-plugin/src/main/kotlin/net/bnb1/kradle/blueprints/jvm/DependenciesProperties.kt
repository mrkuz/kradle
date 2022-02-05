package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class DependenciesProperties : Properties() {

    val useGuava = optional(Catalog.Versions.guava)
    val useCaffeine = optional(Catalog.Versions.caffeine)
    val useLog4j = optional(Catalog.Versions.log4j)
}
