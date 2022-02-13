package net.bnb1.kradle.config.dsl.general

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Optional

class HelmDsl(properties: AllProperties) {

    val releaseName = Optional<String>(null) { properties.helm.releaseName = it }
    val valuesFile = Optional<String>(null) { properties.helm.valuesFile = it }
}
