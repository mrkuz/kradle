package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.blueprints.jvm.JacocoProperties
import net.bnb1.kradle.dsl.Value
import net.bnb1.kradle.dsl.ValueSet

class JacocoDsl(properties: JacocoProperties) {

    val version = Value(properties.version) { properties.version = it }
    val excludes = ValueSet(properties.excludes)
}
