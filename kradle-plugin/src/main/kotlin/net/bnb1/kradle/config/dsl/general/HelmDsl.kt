package net.bnb1.kradle.config.dsl.general

import net.bnb1.kradle.blueprints.general.HelmProperties
import net.bnb1.kradle.dsl.Optional

class HelmDsl(properties: HelmProperties) {

    val releaseName = Optional<String>(null) { properties.releaseName = it }
    val valuesFile = Optional<String>(null) { properties.valuesFile = it }
}
