package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.features.AllProperties

class JavaLintDsl(properties: AllProperties) {

    val checkstyle = Configurable(properties.checkstyle)
}
