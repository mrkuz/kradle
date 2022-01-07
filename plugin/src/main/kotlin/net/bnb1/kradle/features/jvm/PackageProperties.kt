package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.KradleContext
import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties

class PackageProperties(context: KradleContext) : Properties() {

    private val _uberJarProperties by context { PackageUberJarProperties() }
    val uberJar = PropertiesDsl(_uberJarProperties)
}
