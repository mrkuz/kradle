package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class JavaCodeAnalysisProperties(project: Project) : Properties(project) {

    val pmd = PropertiesDsl.Builder<PmdProperties>(project)
        .properties { PmdProperties(it) }
        .build()

    val spotbugs = PropertiesDsl.Builder<SpotBugsProperties>(project)
        .properties { SpotBugsProperties(it) }
        .build()
    val spotBugs = spotbugs
}
