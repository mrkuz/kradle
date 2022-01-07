package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.dsl.PropertiesDsl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class JavaProperties(project: Project) : Properties() {

    val previewFeatures = flag()

    val lint = PropertiesDsl.Builder<JavaLintProperties>(project)
        .properties { JavaLintProperties(it) }
        .build()
    val codeAnalysis = PropertiesDsl.Builder<JavaCodeAnalysisProperties>(project)
        .properties { JavaCodeAnalysisProperties(it) }
        .build()
}
