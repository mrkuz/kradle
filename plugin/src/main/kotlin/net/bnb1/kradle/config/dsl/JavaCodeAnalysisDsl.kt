package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.BlueprintDsl

class JavaCodeAnalysisDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val pmd = BlueprintDsl(blueprints.pmd, properties.pmd)

    val spotbugs = BlueprintDsl(blueprints.spotBugs, properties.spotBugs)
    val spotBugs = spotbugs
}
