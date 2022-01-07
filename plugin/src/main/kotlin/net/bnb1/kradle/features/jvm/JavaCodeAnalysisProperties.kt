package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties

class JavaCodeAnalysisProperties(context: KradleContext) : Properties() {

    private val _pmdProperties by context { PmdProperties() }
    val pmd = PropertiesDsl(_pmdProperties)

    private val _spotBugsProperties by context { SpotBugsProperties() }
    val spotbugs = PropertiesDsl(_spotBugsProperties)
    val spotBugs = spotbugs
}
