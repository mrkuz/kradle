package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class JacocoProperties : Properties() {

    val version = value(Catalog.Versions.jacoco)
    val includes = valueSet<String>()
}
