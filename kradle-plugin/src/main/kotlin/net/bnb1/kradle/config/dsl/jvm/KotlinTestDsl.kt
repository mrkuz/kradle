package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Optional

class KotlinTestDsl(properties: AllProperties) {

    val useKotest = Optional(Catalog.Versions.kotest) { properties.kotlinTest.useKotest = it }
    val useMockk = Optional(Catalog.Versions.mockk) { properties.kotlinTest.useMockk = it }
}
