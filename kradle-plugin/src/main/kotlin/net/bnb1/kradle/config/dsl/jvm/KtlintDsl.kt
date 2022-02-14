package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.KtlintProperties
import net.bnb1.kradle.dsl.InvertedFlagSet
import net.bnb1.kradle.dsl.Value

class KtlintDsl(properties: KtlintProperties) {

    val version = Value(properties.version) { properties.version = it }
    val rules = InvertedFlagSet(properties.ruleExcludes)
}
