package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class KtlintProperties(var version: String) : Properties {

    var ruleExcludes = mutableSetOf<String>()
}
