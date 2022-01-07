package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties

class JavaLintProperties(context: KradleContext) : Properties() {

    private val _checkstyleProperties by context { CheckstyleProperties() }
    val checkstyle = PropertiesDsl(_checkstyleProperties)
}
