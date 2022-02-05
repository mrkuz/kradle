package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class TestProperties : Properties() {

    val prettyPrint = flag()
    val showStandardStreams = flag()

    val withIntegrationTests = flag()
    val withFunctionalTests = flag()
    val withCustomTests = valueSet<String>()

    val useArchUnit = optional(Catalog.Versions.archUnit)
    val useTestcontainers = optional(Catalog.Versions.testcontainers)
}
