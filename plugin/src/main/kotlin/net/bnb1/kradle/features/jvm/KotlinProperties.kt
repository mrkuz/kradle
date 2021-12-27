package net.bnb1.kradle.features.jvm

import Catalog
import net.bnb1.kradle.empty
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.features.PropertiesDslImpl
import org.gradle.api.Project

class KotlinProperties(project: Project) : Properties(project) {

    val kotlinxCoroutinesVersion = property(factory.empty<String>())
    fun useCoroutines(version: String = Catalog.Versions.kotlinCoroutines) = kotlinxCoroutinesVersion.set(version)

    val lint = PropertiesDslImpl(KotlinLintProperties(project))
        .register(project)
        .asInterface()
    val codeAnalysis = PropertiesDslImpl(KotlinCodeAnalysisProperties(project))
        .register(project)
        .asInterface()
    val test = PropertiesDslImpl(KotlinTestProperties(project))
        .register(project)
        .asInterface()
}
