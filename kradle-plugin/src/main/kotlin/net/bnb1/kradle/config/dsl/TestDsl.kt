package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl

class TestDsl(features: AllFeatures, properties: AllProperties) {

    val junitJupiter = FeatureDsl(features.junitJupiter, properties.junitJupiter)
    fun withJunitJupiter(version: String = Catalog.Versions.junit) = junitJupiter.enable {
        version(version)
    }

    val jacoco = FeatureDsl(features.jacoco, properties.jacoco)
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
