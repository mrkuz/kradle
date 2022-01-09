package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class KtlintProperties : Properties() {

    val version = value(Catalog.Versions.ktlint)
    val rules = flags(true)
}
