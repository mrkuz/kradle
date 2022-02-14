package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.ApplicationProperties
import net.bnb1.kradle.dsl.Optional

class ApplicationDsl(properties: ApplicationProperties) {

    val mainClass = Optional<String>(null) { properties.mainClass = it }
}
