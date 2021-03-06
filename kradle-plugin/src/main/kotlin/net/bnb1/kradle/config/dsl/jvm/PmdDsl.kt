package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.PmdProperties
import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Value

class PmdDsl(properties: PmdProperties) {

    val version = Value(properties.version) { properties.version = it }
    val ruleSets = Configurable(RuleSets(properties.ruleSets))

    class RuleSets(ruleSets: PmdProperties.RuleSets) {

        val bestPractices = Flag { ruleSets.bestPractices = it }
        val codeStyle = Flag { ruleSets.codeStyle = it }
        val design = Flag { ruleSets.design = it }
        val documentation = Flag { ruleSets.documentation = it }
        val errorProne = Flag { ruleSets.errorProne = it }.also { it.enable() }
        val multithreading = Flag { ruleSets.multithreading = it }.also { it.enable() }
        val performance = Flag { ruleSets.performance = it }.also { it.enable() }
        val security = Flag { ruleSets.security = it }.also { it.enable() }
    }
}
