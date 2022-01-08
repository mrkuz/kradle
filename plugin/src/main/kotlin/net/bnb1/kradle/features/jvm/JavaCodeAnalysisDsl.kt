package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.features.AllProperties

class JavaCodeAnalysisDsl(properties: AllProperties) {

    val pmd = Configurable(properties.pmd)

    val spotbugs = Configurable(properties.spotBugs)
    val spotBugs = spotbugs
}
