package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class TestProperties : Properties() {

    val prettyPrint = flag()
    val standardStreams = flag()
    val integrationTests = flag()
    val functionalTests = flag()
    val customTests = valueSet<String>()

    val useArchUnit = optional(Catalog.Versions.archUnit)
    val useTestcontainers = optional(Catalog.Versions.testcontainers)
}
