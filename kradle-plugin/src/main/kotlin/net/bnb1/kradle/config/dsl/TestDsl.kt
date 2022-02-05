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
    val showStandardStreams = properties.test.showStandardStreams

    val withIntegrationTests = properties.test.withIntegrationTests
    val integrationTests = withIntegrationTests

    val withFunctionalTests = properties.test.withFunctionalTests
    val functionalTests = withFunctionalTests

    val withCustomTests = properties.test.withCustomTests
    val customTests = withCustomTests

    val useArchUnit = properties.test.useArchUnit
    val useTestcontainers = properties.test.useTestcontainers
}
