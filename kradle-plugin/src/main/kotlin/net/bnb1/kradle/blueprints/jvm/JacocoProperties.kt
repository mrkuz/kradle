package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class JacocoProperties(var version: String) : Properties {

    val excludes = mutableSetOf<String>()
}
