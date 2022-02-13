package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.LintProperties
import net.bnb1.kradle.dsl.Flag

class LintDsl(properties: LintProperties) {

    val ignoreFailures = Flag { properties.ignoreFailures = it }
}
