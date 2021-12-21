package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.empty
import net.bnb1.kradle.features.ConfigurablePropertiesImpl
import net.bnb1.kradle.features.Properties
import org.gradle.api.Project

class KotlinProperties(project: Project) : Properties(project) {

    val kotlinxCoroutinesVersion = property(factory.empty<String>())
    fun useCoroutines(version: String = "1.5.2") = kotlinxCoroutinesVersion.set(version)

    val lint = ConfigurablePropertiesImpl(KotlinLintProperties(project))
        .register(project)
        .asInterface()
    val codeAnalysis = ConfigurablePropertiesImpl(KotlinCodeAnalysisProperties(project))
        .register(project)
        .asInterface()
    val test = ConfigurablePropertiesImpl(KotlinTestProperties(project))
        .register(project)
        .asInterface()
}
