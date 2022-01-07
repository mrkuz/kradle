package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties

class TestProperties : Properties() {

    val withJunitJupiter = optional(Catalog.Versions.junit)
    val withJacoco = optional(Catalog.Versions.jacoco)

    val prettyPrint = flag()

    val withIntegrationTests = flag()
    val integrationTests = withIntegrationTests

    val withFunctionalTests = flag()
    val functionalTests = withFunctionalTests

    val customTests = valueSet<String>()
}
