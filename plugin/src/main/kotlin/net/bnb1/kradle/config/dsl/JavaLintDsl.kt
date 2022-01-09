package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class JavaLintDsl(properties: AllProperties) {

    val checkstyle = Configurable(properties.checkstyle)
}
