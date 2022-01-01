package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.empty
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDsl
import org.gradle.api.Project

class KotlinProperties(project: Project) : Properties(project) {

    val kotlinxCoroutinesVersion = property(factory.empty<String>())
    fun useCoroutines(version: String = Catalog.Versions.kotlinCoroutines) = kotlinxCoroutinesVersion.set(version)

    val lint = PropertiesDsl.Builder<KotlinLintProperties>(project)
        .properties { KotlinLintProperties(it) }
        .build()
    val codeAnalysis = PropertiesDsl.Builder<KotlinCodeAnalysisProperties>(project)
        .properties { KotlinCodeAnalysisProperties(it) }
        .build()
    val test = PropertiesDsl.Builder<KotlinTestProperties>(project)
        .properties { KotlinTestProperties(it) }
        .build()
}