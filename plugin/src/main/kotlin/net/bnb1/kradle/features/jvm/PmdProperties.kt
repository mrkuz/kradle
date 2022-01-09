package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.ConfigurableSelf
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Properties

class PmdProperties : Properties() {

    val version = value(Catalog.Versions.pmd)
    val ruleSets = RuleSets()

    class RuleSets : ConfigurableSelf<RuleSets> {

        val bestPractices = Flag()
        val codeStyle = Flag()
        val design = Flag()
        val documentation = Flag()
        val errorProne = Flag().apply { enable() }
        val multithreading = Flag().apply { enable() }
        val performance = Flag().apply { enable() }
        val security = Flag().apply { enable() }
    }
}
