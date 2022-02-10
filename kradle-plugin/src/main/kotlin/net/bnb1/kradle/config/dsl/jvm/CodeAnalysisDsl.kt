package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Flag

class CodeAnalysisDsl(properties: AllProperties) {

    val ignoreFailures = Flag { properties.codeAnalysis.ignoreFailures = it }
}
