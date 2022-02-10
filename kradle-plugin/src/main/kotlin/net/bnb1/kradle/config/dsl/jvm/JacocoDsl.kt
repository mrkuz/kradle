package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet

class JacocoDsl(properties: AllProperties) {

    val version = Value(Catalog.Versions.jacoco) { properties.jacoco.version = it }
    val excludes = ValueSet(properties.jacoco.excludes)
}
