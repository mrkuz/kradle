package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.InvertedFlagSet
import net.bnb1.kradle.dsl.Value

class KtlintDsl(properties: AllProperties) {

    val version = Value(properties.ktlint.version) { properties.ktlint.version = it }
    val rules = InvertedFlagSet(properties.ktlint.ruleExcludes)
}
