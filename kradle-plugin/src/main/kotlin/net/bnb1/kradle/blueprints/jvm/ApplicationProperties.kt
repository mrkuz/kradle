package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.dsl.Properties

class ApplicationProperties : Properties() {

    val mainClass = optional<String>()
}
