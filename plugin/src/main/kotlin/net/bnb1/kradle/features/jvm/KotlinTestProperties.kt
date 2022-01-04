package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class KotlinTestProperties(project: Project) : Properties(project) {

    val kotestVersion = property<String>()
    fun useKotest(version: String = Catalog.Versions.kotest) = kotestVersion.set(version)

    val mockkVersion = property<String>()
    fun useMockk(version: String = Catalog.Versions.mockk) = mockkVersion.set(version)
}
