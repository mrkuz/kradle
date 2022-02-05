package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class JmhProperties : Properties() {

    val version = value(Catalog.Versions.jmh)
}