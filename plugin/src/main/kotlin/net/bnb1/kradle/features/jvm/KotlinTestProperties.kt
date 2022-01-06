package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class KotlinTestProperties(project: Project) : Properties(project) {

    val useKotest = optional(Catalog.Versions.kotest)
    val useMockk = optional(Catalog.Versions.kotest)
}
