package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDsl
import net.bnb1.kradle.property
import org.gradle.api.Project

class JavaCodeAnalysisProperties(project: Project) : Properties(project) {

    val pmdVersion = property(factory.property(Catalog.Versions.pmd))

    val spotbugs = PropertiesDsl.Builder<SpotBugsProperties>(project)
        .properties { SpotBugsProperties(it) }
        .build()
    val spotBugs = spotbugs
}