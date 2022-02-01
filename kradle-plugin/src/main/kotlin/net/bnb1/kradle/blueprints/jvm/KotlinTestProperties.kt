package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class KotlinTestProperties : Properties() {

    val useKotest = optional(Catalog.Versions.kotest)
    val useMockk = optional(Catalog.Versions.mockk)
}
