package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.InvertedFlags
import net.bnb1.kradle.dsl.Value

class KtlintDsl(properties: AllProperties) {

    val version = Value(Catalog.Versions.ktlint) { properties.ktlint.version = it }
    val rules = InvertedFlags(properties.ktlint.ruleExcludes)
}
