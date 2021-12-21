package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.empty
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class TestProperties(project: Project) : Properties(project) {

    val junitJupiterVersion = property(factory.empty<String>())
    fun withJunitJupiter(version: String = "5.8.1") = junitJupiterVersion.set(version)

    val jacocoVersion = property(factory.empty<String>())
    fun withJacoco(version: String = "0.8.7") = jacocoVersion.set(version)

    val prettyPrint = property(factory.property(false))

    val withIntegrationTests = property(factory.property(false))
    val withFunctionalTests = property(factory.property(false))
}
