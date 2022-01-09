package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class DetektProperties : Properties() {

    val version = value(Catalog.Versions.detekt)
    val configFile = value("detekt-config.yml")
}
