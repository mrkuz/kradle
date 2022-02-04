package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class JavaProperties : Properties() {

    val previewFeatures = flag()
    val useLombok = optional(Catalog.Versions.lombok)
}
