package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class KotlinTestProperties(project: Project) : Properties(project) {

    val useKotest = optionalVersion(Catalog.Versions.kotest)
    val useMockk = optionalVersion(Catalog.Versions.kotest)
}
