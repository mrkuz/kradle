package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class TestProperties(project: Project) : Properties(project) {

    val withJunitJupiter = optionalVersion(Catalog.Versions.junit)
    val withJacoco = optionalVersion(Catalog.Versions.jacoco)

    val prettyPrint = flag()

    val withIntegrationTests = flag()
    val integrationTests = withIntegrationTests

    val withFunctionalTests = flag()
    val functionalTests = withFunctionalTests

    val customTests = setProperty<String>()
    fun withCustomTests(vararg names: String) {
        names.forEach {
            customTests.remove(it)
            customTests.add(it)
        }
    }
}
