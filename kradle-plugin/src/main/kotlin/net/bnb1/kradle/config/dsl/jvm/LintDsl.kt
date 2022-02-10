package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Flag

class LintDsl(properties: AllProperties) {

    val ignoreFailures = Flag { properties.lint.ignoreFailures = it }
}
