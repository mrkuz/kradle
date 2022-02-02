package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.dsl.Properties

class KoverProperties : Properties() {

    val includes = valueSet<String>()
}
