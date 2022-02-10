package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class KtlintProperties : Properties {

    lateinit var version: String
    var ruleExcludes = mutableSetOf<String>()
}
