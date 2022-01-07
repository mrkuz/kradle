package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties

class KotlinTestProperties : Properties() {

    val useKotest = optional(Catalog.Versions.kotest)
    val useMockk = optional(Catalog.Versions.mockk)
}
