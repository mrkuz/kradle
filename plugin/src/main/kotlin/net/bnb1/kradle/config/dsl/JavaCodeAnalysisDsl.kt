package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class JavaCodeAnalysisDsl(properties: AllProperties) {

    val pmd = Configurable(properties.pmd)

    val spotbugs = Configurable(properties.spotBugs)
    val spotBugs = spotbugs
}
