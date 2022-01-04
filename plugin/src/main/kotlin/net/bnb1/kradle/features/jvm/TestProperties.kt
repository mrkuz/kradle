package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class TestProperties(project: Project) : Properties(project) {

    val junitJupiterVersion = property<String>()
    fun withJunitJupiter(version: String = Catalog.Versions.junit) = junitJupiterVersion.set(version)

    val jacocoVersion = property<String>()
    fun withJacoco(version: String = Catalog.Versions.jacoco) = jacocoVersion.set(version)

    val prettyPrint = property(false)

    val withIntegrationTests = property(false)
    fun integrationTests(enabled: Boolean = true) = withIntegrationTests(enabled)

    val withFunctionalTests = property(false)
    fun functionalTests(enabled: Boolean = true) = withFunctionalTests(enabled)

    private val _customTests = mutableListOf<String>()
    val customTests
        get() = _customTests.toList()

    fun withCustomTests(name: String) {
        _customTests.remove(name)
        _customTests.add(name)
    }
}
