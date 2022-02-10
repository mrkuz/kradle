package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Optional

class ApplicationDsl(properties: AllProperties) {

    val mainClass = Optional<String>(null) { properties.application.mainClass = it }
}
