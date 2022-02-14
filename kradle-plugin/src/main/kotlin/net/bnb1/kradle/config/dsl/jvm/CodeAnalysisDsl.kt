package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.CodeAnalysisProperties
import net.bnb1.kradle.dsl.Flag

class CodeAnalysisDsl(properties: CodeAnalysisProperties) {

    val ignoreFailures = Flag { properties.ignoreFailures = it }
}
