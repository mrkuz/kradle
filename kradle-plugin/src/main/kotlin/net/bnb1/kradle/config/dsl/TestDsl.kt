package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.BlueprintDsl

class TestDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val junitJupiter = BlueprintDsl(blueprints.junitJupiter, properties.junitJupiter)
    fun withJunitJupiter(version: String = Catalog.Versions.junit) = junitJupiter.enable {
        version(version)
    }

    val jacoco = BlueprintDsl(blueprints.jacoco, properties.jacoco)
    fun withJacoco(version: String = Catalog.Versions.jacoco) = jacoco.enable {
        version(version)
    }

    val prettyPrint = properties.test.prettyPrint
    val standardStreams = properties.test.standardStreams

    val integrationTests = properties.test.integrationTests
    val withIntegrationTests = integrationTests

    val functionalTests = properties.test.functionalTests
    val withFunctionalTests = functionalTests

    val customTests = properties.test.customTests

    val useArchUnit = properties.test.useArchUnit
    val useTestcontainers = properties.test.useTestcontainers
}
