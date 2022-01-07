package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class JavaCodeAnalysisProperties(project: Project) : Properties() {

    val pmd = PropertiesDsl.Builder<PmdProperties>(project)
        .properties { PmdProperties() }
        .build()

    val spotbugs = PropertiesDsl.Builder<SpotBugsProperties>(project)
        .properties { SpotBugsProperties() }
        .build()
    val spotBugs = spotbugs
}
