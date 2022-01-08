package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties

class JvmProperties : Properties() {

    val targetJvm = value(Catalog.Versions.jvm)
}
