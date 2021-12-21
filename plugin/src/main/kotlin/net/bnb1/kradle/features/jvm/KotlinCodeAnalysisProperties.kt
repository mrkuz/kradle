package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class KotlinCodeAnalysisProperties(project: Project) : Properties(project) {

    val detektConfigFile = property(factory.property("detekt-config.yml"))
    val detektVersion = property(factory.property("1.18.1"))
}