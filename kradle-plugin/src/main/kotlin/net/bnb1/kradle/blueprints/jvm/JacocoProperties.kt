package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class JacocoProperties : Properties {

    lateinit var version: String
    val excludes = mutableSetOf<String>()
}
