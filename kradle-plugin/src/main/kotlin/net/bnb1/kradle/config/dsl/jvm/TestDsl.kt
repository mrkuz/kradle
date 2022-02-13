package net.bnb1.kradle.config.dsl.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.config.AllFeatures
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.core.dsl.FeatureDsl
import net.bnb1.kradle.dsl.Flag
import net.bnb1.kradle.dsl.Optional
import net.bnb1.kradle.dsl.ValueSet

class TestDsl(features: AllFeatures, properties: AllProperties) {

    val junitJupiter = FeatureDsl(features.junitJupiter, JunitJupiterDsl(properties.junitJupiter))
    fun withJunitJupiter(version: String = Catalog.Versions.junit) = junitJupiter.enable {
        version(version)
    }

    val jacoco = FeatureDsl(features.jacoco, JacocoDsl(properties.jacoco))
    fun withJacoco(version: String = Catalog.Versions.jacoco) = jacoco.enable {
        version(version)
    }

    val prettyPrint = Flag { properties.test.prettyPrint = it }
    val showStandardStreams = Flag { properties.test.showStandardStreams = it }

    val withIntegrationTests = Flag { properties.test.withIntegrationTests = it }
    val integrationTests = withIntegrationTests

    val withFunctionalTests = Flag { properties.test.withFunctionalTests = it }
    val functionalTests = withFunctionalTests

    val withCustomTests = ValueSet(properties.test.withCustomTests)
    val customTests = withCustomTests

    val useArchUnit = Optional(Catalog.Versions.archUnit) { properties.test.useArchUnit = it }
    val useTestcontainers = Optional(Catalog.Versions.testcontainers) { properties.test.useTestcontainers = it }
}
