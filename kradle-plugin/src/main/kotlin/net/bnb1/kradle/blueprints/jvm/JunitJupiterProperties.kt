package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class JunitJupiterProperties : Properties() {

    val version = value(Catalog.Versions.junit)
}
