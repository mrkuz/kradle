package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.Properties

class ApplicationProperties : Properties() {

    val mainClass = optional<String>()
}
