package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.blueprints.jvm.KotlinTestProperties
import net.bnb1.kradle.dsl.Optional

class KotlinTestDsl(properties: KotlinTestProperties) {

    val useKotest = Optional(Catalog.Versions.kotest) { properties.useKotest = it }
    val useMockk = Optional(Catalog.Versions.mockk) { properties.useMockk = it }
}
