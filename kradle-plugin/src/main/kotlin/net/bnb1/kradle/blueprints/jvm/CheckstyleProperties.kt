package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class CheckstyleProperties : Properties() {

    val version = value(Catalog.Versions.checkstyle)
    val configFile = value("checkstyle.xml")
}
