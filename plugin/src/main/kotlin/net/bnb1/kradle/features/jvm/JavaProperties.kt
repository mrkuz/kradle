package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDsl
import net.bnb1.kradle.property
import org.gradle.api.Project

class JavaProperties(project: Project) : Properties(project) {

    val withPreviewFeatures = property(factory.property(false))

    val lint = PropertiesDsl.Builder<JavaLintProperties>(project)
        .properties { JavaLintProperties(it) }
        .build()
    val codeAnalysis = PropertiesDsl.Builder<JavaCodeAnalysisProperties>(project)
        .properties { JavaCodeAnalysisProperties(it) }
        .build()
}